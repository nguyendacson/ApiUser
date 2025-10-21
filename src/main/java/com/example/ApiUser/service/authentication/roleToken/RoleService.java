package com.example.ApiUser.service.authentication.roleToken;

import com.example.ApiUser.dto.request.authentication.token.RoleRequest;
import com.example.ApiUser.dto.response.authentication.RoleResponse;
import com.example.ApiUser.mapper.authentication.RoleMapper;
import com.example.ApiUser.repository.authentication.PermissionRepository;
import com.example.ApiUser.repository.authentication.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse createRole(RoleRequest roleRequest){
        var role = roleMapper.toRole(roleRequest);

        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);

        return  roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll(){
        return roleMapper.toListRoleResponse(roleRepository.findAll());
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
