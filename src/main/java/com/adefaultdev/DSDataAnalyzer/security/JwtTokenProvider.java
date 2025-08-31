package com.adefaultdev.DSDataAnalyzer.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * Component for JWT token generation, parsing, and validation.
 * Handles all JWT-related operations for authentication.
 *
 * @since 1.0.0
 * @author ADefaultDev
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expiration;

    /**
     * Constructs JWT token provider with secret and expiration.
     *
     * @param secret Base64-encoded secret key for signing
     * @param expiration Token expiration time in milliseconds
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    /**
     * Generates a JWT token for authenticated user.
     *
     * @param username User's username
     * @param roles List of user's roles
     * @return Signed JWT token string
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts username from JWT token.
     *
     * @param token JWT token string
     * @return Username from token subject
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates JWT token signature and expiration.
     *
     * @param token JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
