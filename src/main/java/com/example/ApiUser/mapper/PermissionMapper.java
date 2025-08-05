package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.request.PermissionRequest;
import com.example.ApiUser.dto.request.UserCreationRequest;
import com.example.ApiUser.dto.request.UserUpdateRequest;
import com.example.ApiUser.dto.response.PermissionResponse;
import com.example.ApiUser.dto.response.UserResponse;
import com.example.ApiUser.entity.Permission;
import com.example.ApiUser.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    List<PermissionResponse> toListPermission(List<Permission> permissions);
}
