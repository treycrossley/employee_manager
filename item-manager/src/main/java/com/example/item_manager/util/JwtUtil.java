package com.example.item_manager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.item_manager.model.User;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String secretKey;
    private final long validityInMilliseconds = 24 * 60 * 60 * 1000; // 1 day in milliseconds

    public String generateToken(String subject, String role) {
        if (role == null || role.isEmpty())
            role = "USER";
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", role)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims decodeJWT(String token) {
        System.out.println(secretKey);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token. " + e.getMessage());
        }
    }

    public String extractUsername(String token) {
        return decodeJWT(token).getSubject();
    }

    public String extractRole(String token) {
        Claims claims = decodeJWT(token);
        return claims.get("role", String.class); // Default to null if the role is not present
    }

    public boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return decodeJWT(token).getExpiration().before(new Date());
    }
}
