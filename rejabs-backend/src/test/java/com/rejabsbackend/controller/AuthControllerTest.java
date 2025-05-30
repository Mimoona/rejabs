package com.rejabsbackend.controller;

import com.rejabsbackend.exception.UnAuthorizedUserException;
import com.rejabsbackend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService mockAuthService;

    @Autowired
    private AuthController authController;

    @Test
    @DirtiesContext
    void getMe_shouldReturnUser_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/auth")
                        .with(oidcLogin().userInfoToken(token -> token
                                        .claim("id", 1234)
                                        .claim("login", "testUser")
                                        .claim("email", "mock@example.com")
                                        .claim("avatar_url", "http://image.png")
                                )

                        )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                         {
                                                         "id": 1234,
                                                         "login": "testUser",
                                                         "email": "mock@example.com",
                                                         "avatar_url": "http://image.png"
                                         
                         }

                        """
                ));
    }


    @Test
    void getMe_shouldThrowUnAuthorizedUserException_whenUserIsNull() throws UnAuthorizedUserException {

        UnAuthorizedUserException thrown = assertThrows(
                UnAuthorizedUserException.class,
                () -> authController.getMe(null)
        );

        assertEquals("User is not authenticated", thrown.getMessage());
    }

    @Test
    void logoutShouldReturn200AndClearSession() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().doesNotExist("JSESSIONID"));
    }

    @Test
    void unauthenticatedRequest_withJsonAccept_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/auth")
                        .header("Accept", "application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedRequest_withoutJsonAccept_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/oauth2/authorization/github"));
    }

    @Test
    void getMe_shouldReturnUnauthorizedError_whenUserIsNull() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User is not authenticated"))
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"));
    }

}