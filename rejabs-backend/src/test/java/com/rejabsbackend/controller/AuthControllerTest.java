package com.rejabsbackend.controller;

import com.rejabsbackend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void logoutShouldReturn200AndClearSession() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk());
    }


    @Test
    void getMe_shouldReturn401WithJsonError_whenUnauthenticatedApiRequest() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isUnauthorized());

    }
}