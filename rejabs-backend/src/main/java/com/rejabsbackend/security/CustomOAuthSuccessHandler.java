package com.rejabsbackend.security;

import java.io.IOException;

import com.rejabsbackend.enums.AuthProvider;
import com.rejabsbackend.model.AppUser;
import com.rejabsbackend.repo.AppUserRepository;
import com.rejabsbackend.service.CustomUserDetails;
import com.rejabsbackend.service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final AppUserRepository appUserRepo;

    public CustomOAuthSuccessHandler(JWTService jwtService, AppUserRepository appUserRepo) {
        this.jwtService = jwtService;
        this.appUserRepo = appUserRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Integer githubId = oAuth2User.getAttribute("id");
        String userId = githubId.toString();
        String username = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email");
        String avatarUrl = oAuth2User.getAttribute("avatar_url");

        AppUser user = appUserRepo.findById(userId).orElseGet(() ->
                appUserRepo.save(new AppUser(userId, username, email, null, avatarUrl, AuthProvider.GITHUB))
        );

        String token = jwtService.generateToken(new CustomUserDetails(user));

        // Redirect with token to frontend (e.g. /oauth2/callback?token=...)
        String redirectUrl = "http://localhost:5173/oauth2/callback?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}
