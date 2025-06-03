package com.rejabsbackend.service;

import com.rejabsbackend.exception.UnAuthorizedUserException;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final AppUserRepository appUserRepo;

    public AuthService(AppUserRepository appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public AppUser saveUser(OAuth2User oAuth2User) throws UnAuthorizedUserException {
       Map<String, Object> attributes = oAuth2User.getAttributes();  //  getAttributes bring whole Object

        Integer id = attributes.get("id") != null ? ((Number) attributes.get("id")).intValue() : 0;
        String username =  attributes.get("login") != null ? (String) attributes.get("login"): null;
        String email = attributes.get("email") != null ? attributes.get("email").toString() : null;
        String avatarUrl = attributes.get("avatar_url") != null ? (String) attributes.get("avatar_url"): null;

        // If user exist already in DB
        Optional<AppUser> existingUser = appUserRepo.findById(id.toString());

        return existingUser.orElseGet(() -> appUserRepo.save(new AppUser(id, username, email, avatarUrl)));

    }

    public String getCurrentUserId() {
        OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getAttribute("id");
    }
}
