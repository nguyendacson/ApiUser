package com.example.ApiUser.controller.Authentication;

import com.example.ApiUser.dto.request.authentication.token.IntrospectRequest;
import com.example.ApiUser.dto.request.authentication.token.RefreshTokenRequest;
import com.example.ApiUser.dto.request.authentication.users.AuthenticationRequest;
import com.example.ApiUser.dto.request.authentication.users.LogoutRequest;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.dto.response.authentication.AuthenticationResponse;
import com.example.ApiUser.dto.response.authentication.IntrospectResponse;
import com.example.ApiUser.entity.authentication.users.EmailVerificationToken;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.authentication.users.EmailVerificationTokenRepository;
import com.example.ApiUser.service.authentication.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    EmailVerificationTokenRepository tokenRepository;
    UserRepository userRepository;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticationResponseApiResponse(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login Success")
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectResponseApiResponse(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        var result = authenticationService.introspectResponse(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest)
            throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshResponseApiResponse(@RequestBody RefreshTokenRequest refreshTokenRequest)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(refreshTokenRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        EmailVerificationToken verification = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        User user = verification.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        tokenRepository.delete(verification);

        return ResponseEntity.ok("""
                    <html>
                        <body style="text-align:center; font-family:Arial, sans-serif;">
                            <h2>Email verified successfully!</h2>
                        </body>
                    </html>
                """);
        //                            <p>You can now <a href="https://your-frontend.com/login">login</a>.</p>
    }
}
