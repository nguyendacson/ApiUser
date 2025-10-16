package com.example.ApiUser.mapper.authentication;

import com.example.ApiUser.dto.request.authentication.token.PermissionRequest;
import com.example.ApiUser.dto.response.authentication.PermissionResponse;
import com.example.ApiUser.entity.authentication.token.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    List<PermissionResponse> toListPermission(List<Permission> permissions);
}
