package com.example.ApiUser.service;

import com.example.ApiUser.dto.request.user.RoleRequest;
import com.example.ApiUser.dto.response.RoleResponse;
import com.example.ApiUser.mapper.RoleMapper;
import com.example.ApiUser.respository.PermissionRepository;
import com.example.ApiUser.respository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    public RoleResponse create(RoleRequest roleRequest){
        var role = roleMapper.toRole(roleRequest);

        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());

        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return  roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        return roleMapper.toListRoleResponse(roleRepository.findAll());
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
