package com.omar.bankapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration class.
 *
 * Defines:
 * - Authentication rules
 * - Authorization rules
 * - JWT filter integration
 * - Password encoder
 */
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Inject JwtFilter into security configuration.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Defines the security filter chain.
     *
     * Security rules:
     * - /api/auth/** → public (login/register)
     * - any other endpoint → authenticated
     * - JWT filter is executed before Spring's authentication filter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Enable CORS
                .cors(cors -> {})

                // Disable CSRF (commonly disabled for REST APIs)
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())

                // Add JWT filter before Spring authentication filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Password encoder bean.
     *
     * BCrypt is the recommended hashing algorithm for storing passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}