package com.omar.bankapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

/**
 * Service responsible for handling all JWT operations.
 *
 * Responsibilities:
 * - Generate JWT tokens after successful login
 * - Extract information from tokens
 * - Validate tokens
 * - Check expiration
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign JWT tokens.
     * Loaded from application.properties
     */
    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Token expiration time in milliseconds.
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Generates signing key from the secret.
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generate JWT token for a specific user.
     *
     * @param username authenticated username
     * @return signed JWT token
     */
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username) // store username in token
                .setIssuedAt(new Date()) // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expiration time
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // sign token
                .compact();
    }

    /**
     * Extract username stored inside JWT token.
     */
    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extract expiration date from token.
     */
    public Date extractExpiration(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    /**
     * Check if token is expired.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate token.
     *
     * Validation rules:
     * - Username inside token must match
     * - Token must not be expired
     */
    public boolean isTokenValid(String token, String username) {

        String extracted = extractUsername(token);

        return extracted.equals(username) && !isTokenExpired(token);
    }
}