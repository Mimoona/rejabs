package com.rejabsbackend.controller;

import com.rejabsbackend.dto.UserLoginDto;
import com.rejabsbackend.dto.UserRegisterDto;
import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.exception.ConflictException;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import com.rejabsbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AppUserRepository appUserRepo;

    public AuthController(AuthService authService, AppUserRepository appUserRepo) {
        this.authService = authService;
        this.appUserRepo = appUserRepo;

    }

//    @GetMapping("/oauth2")
//    public ResponseEntity<Map<String, Object>> getOauthUser(@AuthenticationPrincipal OAuth2User oAuth2User) throws AuthenticationException {
//        if (oAuth2User == null) {
//            throw new AuthenticationException("OAuth2User is null. User not authenticated.");
//        }
//        return authService.saveUser(oAuth2User);
//    }

    @GetMapping("/user")
    public ResponseEntity<AppUser> getCurrentUser() {
        String userId = authService.getCurrentUserId();
        AppUser user = appUserRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterDto registerDto) throws ConflictException {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLoginDto loginDto) {
        return authService.loginUser(loginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // The actual logout processing is handled by Spring Security
        return ResponseEntity.ok().build();
    }
}
