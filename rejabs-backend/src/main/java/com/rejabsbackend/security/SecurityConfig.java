package com.rejabsbackend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for APIs or frontend integration
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth").authenticated() // Protect specific endpoint
                        .anyRequest().permitAll()) // Allow everything else

                .logout(logout -> logout
                        .logoutUrl("/api/logout") // Endpoint to call from frontend
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // Simple success response
                        })
                )

                // Enable OAuth2 login with default success redirect
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("http://localhost:5173/boards", true)) // true = always redirect
                .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    // 👇 Detect if it's an API call (Accept: application/json or X-Requested-With: XMLHttpRequest)
                    String accept = request.getHeader("Accept");
                    if (accept != null && accept.contains("application/json")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    } else {
                        // Default behavior: redirect to login
                        response.sendRedirect("/oauth2/authorization/github");
                    }
                })
        );
        return http.build();
    }
}
