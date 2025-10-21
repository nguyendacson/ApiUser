package com.example.ApiUser.controller.Authentication;

import com.example.ApiUser.dto.request.authentication.token.PermissionRequest;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.dto.response.authentication.PermissionResponse;
import com.example.ApiUser.service.authentication.roleToken.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/permissions")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.allPermission())
                .build();
    }

    @DeleteMapping("/permissions/{name}")
    public ApiResponse<String> delete(@PathVariable String name) {
        permissionService.deletePermission(name);
        return ApiResponse.<String>builder()
                .result("Delete Permission Success")
                .build();
    }
}
