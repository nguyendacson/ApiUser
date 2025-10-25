package com.example.ApiUser.configuration.halder;

import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {

        log.error("OAuth2 Login Failed! Root cause: ", exception);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorCode errorCode = ErrorCode.TKN_VERIFICATION_FAILED;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .success(false)
                .message(errorCode.getMessage())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}
