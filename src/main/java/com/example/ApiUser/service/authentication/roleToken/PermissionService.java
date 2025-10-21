package com.example.ApiUser.service.authentication.roleToken;

import com.example.ApiUser.dto.request.authentication.token.PermissionRequest;
import com.example.ApiUser.dto.response.authentication.PermissionResponse;
import com.example.ApiUser.entity.authentication.token.Permission;
import com.example.ApiUser.mapper.authentication.PermissionMapper;
import com.example.ApiUser.repository.authentication.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse createPermission(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> allPermission(){
        return permissionMapper.toListPermission(permissionRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePermission(String name){
        permissionRepository.deleteById(name);
    }
}