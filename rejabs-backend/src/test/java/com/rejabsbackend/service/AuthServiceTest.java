package com.rejabsbackend.service;

import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


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