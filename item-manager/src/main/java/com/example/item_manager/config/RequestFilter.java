package com.example.item_manager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.item_manager.model.User;
import com.example.item_manager.service.UserService;
import com.example.item_manager.util.JwtUtil;

import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Skip token validation for public endpoints
        if (request.getRequestURI().equals("/api/users/register")
                || request.getRequestURI().equals("/api/users/login")) {
            filterChain.doFilter(request, response); // Proceed without any checks
            return;
        }

        // Extract the token
        final String token = getAccessToken(request);

        // If the token is null, send an unauthorized response
        if (token == null) {
            setUnauthorizedResponse(response, "Unauthorized access");
            return; // Do not call filter chain
        }

        // Extract username and validate token
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username).orElse(null);

        if (jwtUtil.validateToken(token, user)) {
            setAuthenticationContext(token, request); // Set auth context if token is valid
            filterChain.doFilter(request, response); // Continue the filter chain
        } else {
            setUnauthorizedResponse(response, "Unauthorized access"); // Send unauthorized response if invalid token
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    private String getUsername(String token) {
        return jwtUtil.extractUsername(token); // Extract username from the token
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        String username = getUsername(token); // Extract username from the token
        User user = userService.findByUsername(username).orElse(null); // Directly use User

        if (user != null) {
            var authentication = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities()); // Use user directly
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String reason) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + reason + "\"}"); // Simple JSON response
    }
}
