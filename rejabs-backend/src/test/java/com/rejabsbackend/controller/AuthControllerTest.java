package com.rejabsbackend.controller;

import com.rejabsbackend.dto.UserLoginDto;
import com.rejabsbackend.dto.UserRegisterDto;
import com.rejabsbackend.enums.AuthProvider;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import com.rejabsbackend.service.AuthService;
import com.rejabsbackend.service.IdService;
import com.rejabsbackend.service.JWTService;
import com.rejabsbackend.testsupport.SecurityTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@Import(SecurityTestSupport.TestSecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserRepository appUserRepo;
    private final  String baseUrl = "/api/auth";

//    @Test
//    void getOauthUser_WithOAuth2Authentication_ReturnsUserDetails() throws Exception {
//        // Arrange
//        Map<String, Object> userDetails = new HashMap<>();
//        userDetails.put("id", "123456");
//        userDetails.put("username", "githubuser");
//        userDetails.put("email", "github@example.com");
//        userDetails.put("avatarUrl", "https://avatar.url");
//        userDetails.put("provider", "GITHUB");
//        // Omit password since it's null
//
//        Map<String, Object> expectedResponse = new HashMap<>();
//        expectedResponse.put("token", "mock-jwt-token");
//        expectedResponse.put("user", userDetails);
//
//        // Create mock OAuth2 user
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("login", "githubuser");
//        attributes.put("email", "github@example.com");
//        attributes.put("avatar_url", "https://avatar.url");
//        attributes.put("id", 123456);  // As Integer
//
//        // Act & Assert
//        mockMvc.perform(get(baseUrl + "/user")
//                        .with(SecurityTestSupport.getOAuthLogin()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
//                .andExpect(jsonPath("$.user.email").value("github@example.com"))
//                .andExpect(jsonPath("$.user.provider").value("GITHUB"))
//                .andExpect(jsonPath("$.user.password").doesNotExist());
//
//    }

//    @Test
//    void shouldReturnCurrentUser_whenUserIsAuthenticated() throws Exception {
//       AppUser mockUser = new AppUser(
//                "12345",
//                "testuser",
//                "test@example.com",
//                null,
//                "http://avatar.url",
//                null
//        );
//        Mockito.when(authService.getCurrentUserId()).thenReturn("12345");
//        Mockito.when(appUserRepo.findById("12345")).thenReturn(Optional.of(mockUser));
//
//        mockMvc.perform(get(baseUrl+"/user")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("12345"))
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.user.provider").value("GITHUB"));
//    }
//
//    @Test
//    void getCurrentUser_shouldReturn404_whenUserNotFound() throws Exception {
//        Mockito.when(authService.getCurrentUserId()).thenReturn("99999");
//        Mockito.when(appUserRepo.findById("99999")).thenReturn(Optional.empty());
//
//        mockMvc.perform(get(baseUrl+"/user")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }

    @Test
    void register_shouldCreateUser() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto(
                "newUser", "new@example.com", "password");

        Map<String, Object> mockResponse = Map.of(
                "token", "mock-token",
                "user", Map.of("email", "new@example.com")
        );

        when(authService.register(any())).thenReturn(ResponseEntity.ok(mockResponse));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "newUser",
                                    "email": "new@example.com",
                                    "password": "password",
                                    "avatarUrl": "http://avatar.com"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_shouldAuthenticateUser() throws Exception {
        UserLoginDto loginDto = new UserLoginDto("user@example.com", "password");

        Map<String, Object> mockResponse = Map.of(
                "token", "mock-jwt-token",
                "user", loginDto
        );

        when(authService.loginUser(any())).thenReturn(ResponseEntity.ok(mockResponse));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "user@example.com",
                                    "password": "password"
                                }
                                """)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.user.email").value("user@example.com"));
    }

    @Test
    void logout_ShouldReturn200AndClearSession() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());
    }
}