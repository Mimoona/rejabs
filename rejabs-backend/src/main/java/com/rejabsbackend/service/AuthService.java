package com.rejabsbackend.service;

import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
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

    public AppUser saveUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();  //  getAttributes bring whole Object

        String id = attributes.get("id").toString();

        // If user exist already in DB
        Optional<AppUser> existingUser = appUserRepo.findById(id);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        AppUser newUser = new AppUser(
                attributes.get("id").toString(),
                (String) attributes.get("login"),
                attributes.get("email") != null ? attributes.get("email").toString() : null,
                (String) attributes.get("avatar_url")

        );
        return appUserRepo.save(newUser);
    }


}
