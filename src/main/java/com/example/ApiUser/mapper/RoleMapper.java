package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.request.user.RoleRequest;
import com.example.ApiUser.dto.response.RoleResponse;
import com.example.ApiUser.entity.user.Role;
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
