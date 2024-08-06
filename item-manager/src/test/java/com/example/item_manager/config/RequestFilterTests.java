package com.example.item_manager.config;

import com.example.item_manager.model.User;
import com.example.item_manager.service.UserService;
import com.example.item_manager.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RequestFilterTests {

    @InjectMocks
    private RequestFilter requestFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private PrintWriter writer; // Mock PrintWriter

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternal_PublicEndpoint() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/users/register");

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(anyInt()); // Ensure unauthorized response is not called
    }

    @Test
    void testDoFilterInternal_NoToken() throws Exception {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-protected-endpoint");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer); // Mock the writer

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(writer).write("{\"error\": \"Unauthorized access\"}"); // Check the response
        verify(filterChain, never()).doFilter(request, response); // Ensure filter chain is not called
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws Exception {
        // Arrange
        String invalidToken = "invalid.token";
        when(request.getRequestURI()).thenReturn("/api/some-protected-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtUtil.extractUsername(invalidToken)).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(new User()));
        when(jwtUtil.validateToken(eq(invalidToken), any(User.class))).thenReturn(false); // Simulate invalid token
        when(response.getWriter()).thenReturn(writer); // Mock the writer

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(writer).write("{\"error\": \"Unauthorized access\"}");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws Exception {
        // Arrange
        String validToken = "valid.token";
        User user = new User(); // Create a mock user
        user.setUsername("testUser");

        when(request.getRequestURI()).thenReturn("/api/some-protected-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.extractUsername(validToken)).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtUtil.validateToken(eq(validToken), any(User.class))).thenReturn(true);

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication); // Ensure the authentication is not null
        assertEquals(user.getUsername(), authentication.getName()); // Verify the username is set correctly
        verify(filterChain).doFilter(request, response); // Ensure filter chain is called for valid token
    }
}
