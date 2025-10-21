package com.example.ApiUser.controller.admin;

import com.example.ApiUser.dto.response.admin.CountMovie;
import com.example.ApiUser.dto.response.admin.UserResponseAdmin;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.service.admin.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("admin")
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    AdminService adminService;

    @GetMapping("/allUser")
    ApiResponse<List<UserResponseAdmin>> getAllUser() {
        return ApiResponse.<List<UserResponseAdmin>>builder()
                .result(adminService.getAllUser())
                .build();
    }

    @GetMapping("/userInfo/{key}")
    ApiResponse<UserResponseAdmin> userInfor(@PathVariable String key) {
        return ApiResponse.<UserResponseAdmin>builder()
                .result(adminService.userInfor(key))
                .build();
    }

    @GetMapping("/watching")
    ApiResponse<List<CountMovie>> watching() {
        List<CountMovie> list = adminService.getAllMovieWatching();
        return ApiResponse.<List<CountMovie>>builder()
                .result(list)
                .build();
    }

    @GetMapping("/like")
    ApiResponse<List<CountMovie>> like() {
        List<CountMovie> list = adminService.getAllMovieLike();
        return ApiResponse.<List<CountMovie>>builder()
                .result(list)
                .build();
    }

}