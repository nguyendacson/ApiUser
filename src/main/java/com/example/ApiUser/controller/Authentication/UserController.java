package com.example.ApiUser.controller.Authentication;

import com.example.ApiUser.dto.request.authentication.users.*;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.dto.response.authentication.UserResponse;
import com.example.ApiUser.service.authentication.users.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("users")
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest creationRequest) throws MessagingException {
        return ApiResponse.<UserResponse>builder()
                .message("Please go your Email must verify Email!")
                .result(userService.createUser(creationRequest))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(jwt.getClaimAsString("userId")))
                .build();
    }

    @PutMapping("/update")
    ApiResponse<String> updateUser(@AuthenticationPrincipal Jwt jwt,
                                   @RequestBody UserUpdateRequest userUpdateRequest) {

        String userId = jwt.getClaimAsString("userId");
        userService.updateUser(userId, userUpdateRequest);
        return ApiResponse.<String>builder()
                .success(true)
                .message("Update user seccess")
                .build();
    }

    @PutMapping("/avatar")
    ApiResponse<String> updateAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, String> uploadInfo) {

        String userId = jwt.getClaimAsString("userId");
        String result = userService.updateAvatar(userId, uploadInfo);

        return ApiResponse.<String>builder()
                .result(result)
                .build();
    }

    @PostMapping("/forgot-password")
    ApiResponse<String> forgotPassword(@RequestBody ForgotRequest forgotRequest) {
        try {
            userService.forgotPassword(forgotRequest);
        } catch (MessagingException e) {
            return ApiResponse.<String>builder()
                    .result("Please check your email must reset Password")
                    .build();
        }
        return ApiResponse.<String>builder()
                .result("Reset password success")
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<String> resetPassword(@RequestBody ResetPassRequest resetPassRequest) {
        userService.resetPassword(resetPassRequest);
        return ApiResponse.<String>builder()
                .result("Reset password success, You can login as new Password!")
                .build();
    }

    @PostMapping("/change-password")
    ApiResponse<String> changePassword(@Valid
                                       @AuthenticationPrincipal Jwt jwt,
                                       @RequestBody UserChangePassword userChangePassword) {
        String userId = jwt.getClaimAsString("userId");
        userService.changePassword(userId, userChangePassword);
        return ApiResponse.<String>builder()
                .result("Change password Success!")
                .build();
    }

    @DeleteMapping("/delete")
    ApiResponse<String> deleteUser(@AuthenticationPrincipal Jwt jwt) {
        userService.deleteUser(jwt.getClaimAsString("userId"));

        return ApiResponse.<String>builder()
                .result("You has been deleted My Account!")
                .build();
    }
}