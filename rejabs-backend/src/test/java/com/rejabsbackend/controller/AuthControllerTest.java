package com.rejabsbackend.controller;

import com.rejabsbackend.exception.UnAuthorizedUserException;
import com.rejabsbackend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
                .andExpect(status().isOk());
    }

    @Test
    void shouldRedirectToLogin_whenUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getMe_shouldRedirected_whenUserIsNull() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isFound())  // 302
                .andExpect(header().string("Location", "/oauth2/authorization/github"));
    }

    @Test
    void getMe_shouldReturn401WithJsonError_whenUnauthenticatedApiRequest() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isUnauthorized());

    }
}