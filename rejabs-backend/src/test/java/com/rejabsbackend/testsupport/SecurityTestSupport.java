package com.rejabsbackend.testsupport;

import com.rejabsbackend.service.AuthService;
import com.rejabsbackend.service.JWTService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;

public class SecurityTestSupport {
    @TestConfiguration
    public static class TestSecurityConfig {

        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }

        @Bean
        public JWTService jwtService() {
            return Mockito.mock(JWTService.class);
        }

        // Add other mocks as needed
    }

    public static RequestPostProcessor getOAuthLogin() {
        return oauth2Login().attributes(attrs -> {
            attrs.put("id", 12345);
            attrs.put("login", "testUser");
            attrs.put("email", "test@example.com");
            attrs.put("avatar_url", "http://image.png");
        });
    }
}
