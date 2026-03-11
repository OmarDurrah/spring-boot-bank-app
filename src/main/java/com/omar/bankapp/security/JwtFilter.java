package com.omar.bankapp.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter
 *
 * This filter intercepts every incoming HTTP request and checks
 * whether a valid JWT token is present in the Authorization header.
 *
 * If the token is valid, the filter creates an Authentication object
 * and stores it inside Spring Security's SecurityContext.
 *
 * This allows Spring Security to treat the request as authenticated.
 */
@Component
public class JwtFilter implements Filter {

    private final JwtService jwtService;

    /**
     * Constructor-based dependency injection of JwtService.
     */
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Core filter method executed for every HTTP request.
     *
     * Steps:
     * 1. Extract Authorization header.
     * 2. Check if it starts with "Bearer ".
     * 3. Extract the JWT token.
     * 4. Validate the token using JwtService.
     * 5. If valid → create authentication object and store in SecurityContext.
     * 6. Continue the filter chain.
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        // Cast generic request to HttpServletRequest
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Read Authorization header
        String header = httpRequest.getHeader("Authorization");

        // Check if header contains Bearer token
        if (header != null && header.startsWith("Bearer ")) {

            // Extract JWT token (remove "Bearer ")
            String token = header.substring(7);

            // Extract username from token
            String username = jwtService.extractUsername(token);

            // Validate token
            if (username != null && jwtService.isTokenValid(token, username)) {

                // Create authentication object
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username, null, Collections.emptyList());

                // Store authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continue request processing in the filter chain
        chain.doFilter(request, response);
    }
}