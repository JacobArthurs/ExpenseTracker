package com.JacobArthurs.ExpenseTracker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTests {
    private static SecretKey secretKey;

    @BeforeAll
    static void setUp() {
        var secretString = "XNVbDfrGdQY1atY1VoI6aBwc3N8sA2d3ssMTzslyYHA=";
        JwtTokenUtil.SECRET_KEY = secretString;
        secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    @Test
    void generateToken_ValidUsername_ReturnsNotNullToken() {
        var username = "testUser";
        var token = JwtTokenUtil.generateToken(username);
        assertNotNull(token);

        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(username, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertEquals("Expense Tracker", claims.getIssuer());
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        var username = "testUser";
        var token = JwtTokenUtil.generateToken(username);
        assertTrue(JwtTokenUtil.validateToken(token));
    }

    @Test
    void validateToken_ExpiredToken_ReturnsFalse() {
        var expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJFeHBlbnNlIFRyYWNrZXIiLCJzdWIiOiJkZWZhdWx0IiwiaWF0IjoxNzA0MDY3MjAwLCJleHAiOjE3MDQ5MzEyMDB9.KbcnHmTib8JQlsoZGa1h57M82ZnjYHWlp64V-TMD9wA";
        assertFalse(JwtTokenUtil.validateToken(expiredToken));
    }

    @Test
    void getUsernameFromToken_ValidToken_ReturnsUsername() {
        var username = "testUser";
        var token = JwtTokenUtil.generateToken(username);
        assertEquals(username, JwtTokenUtil.getUsernameFromToken(token));
    }
}
