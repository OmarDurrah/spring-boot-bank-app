package com.omar.bankapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;

import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) configuration.
 *
 * Allows the frontend application (React) to communicate
 * with the backend API running on a different origin.
 *
 * Example:
 * Frontend → http://localhost:5173
 * Backend  → http://localhost:6060
 */
@Configuration
public class CorsConfig {

    /**
     * Define global CORS configuration.
     *
     * Allowed:
     * - Origin: React development server
     * - Methods: GET, POST, PUT, DELETE, OPTIONS
     * - Headers: all
     * - Credentials: allowed (cookies/auth headers)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Allow React development server
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));

        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (Authorization header, cookies)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Apply this configuration to all endpoints
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}