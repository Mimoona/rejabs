package com.rejabsbackend.service;

import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AppUserRepository mockAppUserRepo;

    @Autowired
    private AuthService authService;

    private OAuth2User mockOAuth2User(Map<String, Object> attributes) {
        return new DefaultOAuth2User(
                Collections.emptyList(),
                //if have roles
               // List.of(new SimpleGrantedAuthority("ROLE_USER"))
                attributes,
                "login"
        );
    }


    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void isUserAuthenticated_shouldReturnFalse_whenAuthenticationIsNull() {
        SecurityContextHolder.clearContext(); // explicitly ensure null
        assertFalse(authService.isUserAuthenticated());
    }

    @Test
    void isUserAuthenticated_shouldReturnFalse_whenAuthenticationIsNotAuthenticated() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        assertFalse(authService.isUserAuthenticated());
    }

    @Test
    void isUserAuthenticated_shouldReturnFalse_whenPrincipalIsNotOAuth2User() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(mockAuth.getPrincipal()).thenReturn("not-oauth-user");

        SecurityContextHolder.getContext().setAuthentication(mockAuth);
        assertFalse(authService.isUserAuthenticated());
    }

    @Test
    void isUserAuthenticated_shouldReturnTrue_whenAuthenticatedOAuth2User() {
        Map<String, Object> attributes = Map.of("login", "testuser");
        OAuth2User user = new DefaultOAuth2User(
                Collections.emptyList(),
                attributes,
                "login"
        );

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(mockAuth.getPrincipal()).thenReturn(user);

        SecurityContextHolder.getContext().setAuthentication(mockAuth);
        assertTrue(authService.isUserAuthenticated());
    }



    @Test
    void saveUser_shouldSaveNewUser_whenNotExists() {
        Map<String, Object> attributes = Map.of(
                "id", 1234,
                "login", "testUser",
                "email", "test@example.com",
                "avatar_url", "http://avatar.com"
        );
        OAuth2User user = mockOAuth2User(attributes);

        AppUser saved = authService.saveUser(user);

        assertEquals(1234, saved.id());
        assertEquals("testUser", saved.login());
        assertEquals("test@example.com", saved.email());
        assertEquals("http://avatar.com", saved.avatar_url());
    }

    @Test
    void saveUser_shouldReturnExistingUser_whenAlreadyExists() {
        AppUser existing = new AppUser(1234, "existingUser", "exist@example.com", "http://avatar.png");
        mockAppUserRepo.save(existing);

        Map<String, Object> attributes = Map.of(
                "id", 1234,
                "login", "existingUser",
                "email", "exist@example.com",
                "avatar_url", "http://avatar.png"
        );
        OAuth2User user = mockOAuth2User(attributes);

        AppUser actual = authService.saveUser(user);

        assertEquals(existing, actual);
    }

}