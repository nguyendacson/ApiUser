package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.request.PermissionRequest;
import com.example.ApiUser.dto.request.RoleRequest;
import com.example.ApiUser.dto.response.PermissionResponse;
import com.example.ApiUser.dto.response.RoleResponse;
import com.example.ApiUser.entity.Permission;
import com.example.ApiUser.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toListRoleResponse(List<Role> roles);
}
