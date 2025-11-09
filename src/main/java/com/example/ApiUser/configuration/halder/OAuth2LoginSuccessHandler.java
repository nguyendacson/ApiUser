package com.example.ApiUser.configuration.halder;

import com.example.ApiUser.configuration.config.CustomOAuth2UserPrincipal;
import com.example.ApiUser.dto.response.authentication.AuthenticationResponse;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.service.authentication.roleToken.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2UserPrincipal principal = (CustomOAuth2UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        if (!Objects.equals(user.getProvider(), "Google")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid provider");
            return;
        }

        var accessToken = authenticationService.generateToken(user);
        var refreshToken = authenticationService.generateRefreshToken(user);

        String redirectUrl = String.format(
                "movieseeme://auth/callback?accessToken=%s&refreshToken=%s",
                URLEncoder.encode(accessToken, StandardCharsets.UTF_8),
                URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
        );

        response.sendRedirect(redirectUrl);

//        AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken, true);
//
//        // Trả JSON cho client
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
//        response.getWriter().flush();
    }
}
