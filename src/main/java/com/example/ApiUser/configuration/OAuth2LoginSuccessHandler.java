package com.example.ApiUser.configuration;

import com.example.ApiUser.dto.response.authentication.AuthenticationResponse;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.service.authentication.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    AuthenticationService authenticationService;
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmailWithRolesAndPermissions(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Objects.equals(user.getProvider(), "Google")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid provider");
            return;
        }

        // Sinh JWT token
        var accessToken = authenticationService.generateToken(user);
        var refreshToken = authenticationService.generateToken(user);
        AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken, true);

        // Trả JSON cho client
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
        response.getWriter().flush();
    }
}
