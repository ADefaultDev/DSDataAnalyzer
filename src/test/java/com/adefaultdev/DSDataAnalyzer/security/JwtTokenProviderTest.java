package com.adefaultdev.DSDataAnalyzer.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link JwtTokenProvider}.
 *
 * @since 1.2.0
 * @author ADefaultDev
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private final String secret = Base64.getEncoder().encodeToString("super-secret-key-for-tests-123456".getBytes());

    @BeforeEach
    void setUp() {
        long expiration = 1000 * 60 * 60;
        jwtTokenProvider = new JwtTokenProvider(secret, expiration);
    }

    @Test
    void generateToken_shouldContainUsernameAndRoles() {
        String token = jwtTokenProvider.generateToken("testuser", List.of("ROLE_USER", "ROLE_ADMIN"));

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));

        String username = jwtTokenProvider.getUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void validateToken_shouldReturnFalseForTamperedToken() {
        String token = jwtTokenProvider.generateToken("testuser", List.of("ROLE_USER"));

        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertFalse(jwtTokenProvider.validateToken(tampered));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(secret, 1);
        String token = shortLivedProvider.generateToken("testuser", List.of("ROLE_USER"));

        Thread.sleep(10);

        assertFalse(shortLivedProvider.validateToken(token));
    }
}
