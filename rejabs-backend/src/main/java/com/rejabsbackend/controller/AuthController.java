package com.rejabsbackend.controller;

import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping()
    public AppUser getMe(@AuthenticationPrincipal OAuth2User oAuth2User) throws AuthenticationException {
        return authService.saveUser(oAuth2User);
    }
}
