package com.rejabsbackend.service;

import com.rejabsbackend.dto.UserLoginDto;
import com.rejabsbackend.dto.UserRegisterDto;
import com.rejabsbackend.enums.AuthProvider;
import com.rejabsbackend.exception.AuthenticationException;
import com.rejabsbackend.exception.ConflictException;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final AppUserRepository appUserRepo;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final IdService idService;

    public AuthService(AppUserRepository appUserRepo, PasswordEncoder encoder, JWTService jwtService, IdService idService ) {
        this.appUserRepo = appUserRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.idService =idService;
    }

//    public ResponseEntity<Map<String,Object>> saveUser(OAuth2User oAuth2User) throws AuthenticationException {
//
//        Integer githubId = oAuth2User.getAttribute("id");
//        if (githubId == null) {
//            throw new AuthenticationException("GitHub ID not found in OAuth2 attributes");
//        }
//
//        String username = oAuth2User.getAttribute("login");
//        String email = oAuth2User.getAttribute("email");
//        String avatarUrl = oAuth2User.getAttribute("avatar_url");
//
//        // Convert ID to String
//        String userId = githubId.toString();
//        // If user exist already in DB
//        AppUser user = appUserRepo.findById(userId).orElseGet(() -> {
//            AppUser newUser = new AppUser(userId, username, email, null, avatarUrl, AuthProvider.GITHUB);
//            return appUserRepo.save(newUser);
//        });
//
//        String token = jwtService.generateToken(new CustomUserDetails(user));
//        return ResponseEntity.ok(Map.of("user", user,"token", token));
//
//    }

    public boolean isUserAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null &&
//                authentication.isAuthenticated() &&
//                authentication.getPrincipal() instanceof OAuth2User;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        // Acceptable principal types for authenticated users:
        return (principal instanceof OAuth2User) ||
                (principal instanceof UserDetails) ||
                (principal instanceof String && !"anonymousUser".equals(principal));
    }

    public String getCurrentUserId() {
        if (!isUserAuthenticated()) {
            throw new AuthenticationException("User is not properly authenticated");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User user = (OAuth2User) principal;
            Object idAttribute = user.getAttribute("id");
            if (idAttribute == null) {
                throw new AuthenticationException("User ID not found in OAuth2 attributes");
            }
            return idAttribute.toString();
        }
        else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        else if (principal instanceof String) {
            // principal can be the username string (avoid anonymousUser)
            String username = (String) principal;
            if ("anonymousUser".equals(username)) {
                throw new AuthenticationException("User is anonymous");
            }
            return username;
        }
        else {
            throw new AuthenticationException("Unknown principal type: " + principal.getClass());
        }

//        try {
//            OAuth2User user = (OAuth2User) SecurityContextHolder.getContext()
//                    .getAuthentication()
//                    .getPrincipal();
//
//            Object idAttribute = user.getAttribute("id");
//
//            if (idAttribute == null) {
//                throw new AuthenticationException("User ID not found in OAuth2 attributes");
//            }
//
//            return idAttribute.toString();
//        } catch (Exception e) {
//            throw new AuthenticationException("Failed to get current user ID: " + e.getMessage());
//        }
    }

    public ResponseEntity<Map<String, Object>> register(UserRegisterDto registerDto) throws ConflictException {
        if (appUserRepo.findByEmail(registerDto.email()).isPresent())
            throw new ConflictException("Email already registered");

        AppUser user = new AppUser(
                idService.generateId(),
                registerDto.username(),
                registerDto.email(),
                encoder.encode(registerDto.password()),
                null,
                AuthProvider.LOCAL
        );
        appUserRepo.save(user);

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return ResponseEntity.ok(Map.of("user", user, "token", token));
    }

    public ResponseEntity<Map<String, Object>> loginUser(UserLoginDto loginDto)  {
        AppUser user = appUserRepo.findByEmail(loginDto.email())
                .filter(u -> u.provider() == AuthProvider.LOCAL)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!encoder.matches(loginDto.password(), user.password()))
            throw new AuthenticationException("Wrong password");

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return ResponseEntity.ok(Map.of("user", user,"token", token));
    }
}