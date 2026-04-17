package com.flipkart.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for role-based authentication.
 * ADMIN users can access /admin/** pages.
 * USER role can access cart, checkout, orders.
 * Public pages are accessible to everyone.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.dao.DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        org.springframework.security.authentication.dao.DaoAuthenticationProvider authProvider = 
            new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // URL-based authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public pages - accessible without login
                .requestMatchers("/", "/home", "/products", "/product/**",
                    "/signup", "/login", "/forgot-password", "/reset-password",
                    "/about", "/contact", "/search",
                    "/careers", "/payments", "/shipping", "/faq",
                    "/returns", "/terms", "/privacy",
                    "/css/**", "/js/**", "/images/**", "/favicon.png").permitAll()
                // Admin pages - only ADMIN role
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Cart, checkout, orders - logged-in users (USER or ADMIN)
                .requestMatchers("/cart/**", "/checkout/**", "/orders/**",
                    "/review/**").hasAnyRole("USER", "ADMIN")
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            // Form-based login configuration
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            // Logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
