package com.rejabsbackend.service;

import com.rejabsbackend.enums.AuthProvider;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {


    AppUserRepository mockAppUserRepo = Mockito.mock(AppUserRepository.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
    JWTService jwtService = Mockito.mock(JWTService.class);
    IdService idService = Mockito.mock(IdService.class);
    AuthService authService = new AuthService(mockAppUserRepo, encoder, jwtService, idService);

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

//    @Test
//    void isUserAuthenticated_shouldReturnFalse_whenPrincipalIsNotOAuth2User() {
//        Authentication mockAuth = mock(Authentication.class);
//        when(mockAuth.isAuthenticated()).thenReturn(true);
//        when(mockAuth.getPrincipal()).thenReturn("not-oauth-user");
//
//        SecurityContextHolder.getContext().setAuthentication(mockAuth);
//        assertFalse(authService.isUserAuthenticated());
//    }

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


//    @Test
//    void saveUser_shouldSaveNewUser_whenNotExists() {
//        Map<String, Object> attributes = Map.of(
//                "id", 33322,
//                "login", "newUser",
//                "email", "new@example.com",
//                "avatar_url", "http://avatar.com"
//        );
//        OAuth2User user = mockOAuth2User(attributes);
//
//        AppUser newUser = new AppUser("33322", "newUser", "new@example.com", null, "http://avatar.com", AuthProvider.GITHUB);
//
//        Mockito.when(mockAppUserRepo.findById("33322")).thenReturn(Optional.empty());
//        Mockito.when(mockAppUserRepo.save(Mockito.any())).thenReturn(newUser);
//        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("mock-token-33322");
//
//        ResponseEntity<Map<String, Object>> response = authService.saveUser(user);
//
//        assertEquals("mock-token-33322", response.getBody().get("token"));
//        assertEquals(newUser, response.getBody().get("user"));
//    }

//    @Test
//    void saveUser_shouldReturnExistingUser_whenAlreadyExists() {
//        Map<String, Object> attributes = Map.of(
//                "id", 1234,
//                "login", "existingUser",
//                "email", "exist@example.com",
//                "avatar_url", "http://avatar.png"
//        );
//        OAuth2User user = mockOAuth2User(attributes);
//        AppUser existingUser = new AppUser("1234", "existingUser", "exist@example.com", null, "http://avatar.png", AuthProvider.GITHUB);
//
//        when(mockAppUserRepo.findById("1234")).thenReturn(Optional.of(existingUser));
//        when(jwtService.generateToken(Mockito.any())).thenReturn("mock-token");
//
//        ResponseEntity<Map<String, Object>> response = authService.saveUser(user);
//
//        assertEquals("mock-token", response.getBody().get("token"));
//        assertEquals(existingUser, response.getBody().get("user"));
//    }

}