package com.example.ApiUser.controller;

import com.example.ApiUser.dto.response.ApiResponse;
import com.example.ApiUser.dto.request.user.UserCreationRequest;
import com.example.ApiUser.dto.request.user.UserUpdateRequest;
import com.example.ApiUser.dto.response.UserResponse;
import com.example.ApiUser.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("users")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest creationRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(creationRequest))
                .build();
    }

    @GetMapping
        ApiResponse<List<UserResponse>> getUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        log.info("ON MyInfo");
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId,@RequestBody UserUpdateRequest userUpdateRequest){
        log.info("In Update");
        return userService.updateUser(userId,userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
