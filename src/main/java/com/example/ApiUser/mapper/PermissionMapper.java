package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.request.user.PermissionRequest;
import com.example.ApiUser.dto.response.PermissionResponse;
import com.example.ApiUser.entity.user.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    List<PermissionResponse> toListPermission(List<Permission> permissions);
}
