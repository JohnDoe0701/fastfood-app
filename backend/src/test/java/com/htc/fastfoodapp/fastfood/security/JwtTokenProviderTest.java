package com.htc.fastfoodapp.fastfood.security;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "your-super-secret-key-min-32-characters-for-hs256-algorithm-production-use-strong-random-key");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", 86400000L); // 24 hours

        userDetails = new User("john@example.com", "password", new ArrayList<>());
    }

    @Test
    void testGenerateTokenSuccess() {
        String token = jwtTokenProvider.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void testGetEmailFromTokenSuccess() {
        String token = jwtTokenProvider.generateToken(userDetails);
        String email = jwtTokenProvider.getEmailFromToken(token);

        assertEquals("john@example.com", email);
    }

    @Test
    void testValidateTokenSuccess() {
        String token = jwtTokenProvider.generateToken(userDetails);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpiredFalse() {
        String token = jwtTokenProvider.generateToken(userDetails);

        boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testGetExpirationDateFromTokenSuccess() {
        String token = jwtTokenProvider.generateToken(userDetails);
        Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testGenerateTokenFromUsernameSuccess() {
        String token = jwtTokenProvider.generateTokenFromUsername("test@example.com");

        assertNotNull(token);
        String email = jwtTokenProvider.getEmailFromToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    void testValidateTokenWithMalformedToken() {
        String malformedToken = "malformed.jwt.token";

        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        assertFalse(isValid);
    }
}
