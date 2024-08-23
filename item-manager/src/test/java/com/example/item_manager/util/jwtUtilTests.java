package com.example.item_manager.util;

import com.example.item_manager.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTests {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String secretKey = "P0YfGdONKAXq8bHFO6IoIHZOhkPiNEeOi1dvnf+Ot9M=";
    private User testUser;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Initialize the testUser
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setRole("USER");

        // Use reflection to set the secretKey field in JwtUtil
        Field secretKeyField = JwtUtil.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtUtil, secretKey);

        // Generate a token for the test user
        token = jwtUtil.generateToken(testUser.getUsername(), testUser.getRole());
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token);
    }

    @Test
    void testDecodeJWT() {
        Claims claims = jwtUtil.decodeJWT(token);
        assertEquals("testUser", claims.getSubject());
        assertEquals("USER", claims.get("role"));
    }

    @Test
    void testExtractUsername() {
        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testExtractRole() {
        String role = jwtUtil.extractRole(token);
        assertEquals("USER", role);
    }

    @Test
    void testValidateToken() {
        assertTrue(jwtUtil.validateToken(token, testUser));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        // Mock the expiration date to simulate an expired token
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() - 1000); // Set expiry date to past

        Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        String expiredToken = Jwts.builder()
                .setSubject(testUser.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", "USER")
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        try {
            jwtUtil.validateToken(expiredToken, testUser);
            fail("Expected RuntimeException due to expired token");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Invalid JWT token") && e.getMessage().contains("expired"),
                    "Expected message indicating that the token has expired.");
        }
    }

    @Test
    void testDecodeJWT_InvalidToken() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.decodeJWT("invalid.token");
        });

        assertTrue(exception.getMessage().contains("Invalid JWT token"));
    }
}
