package com.example.ApiUser.service;

import com.example.ApiUser.dto.request.PermissionRequest;
import com.example.ApiUser.dto.request.UserCreationRequest;
import com.example.ApiUser.dto.request.UserUpdateRequest;
import com.example.ApiUser.dto.response.PermissionResponse;
import com.example.ApiUser.dto.response.UserResponse;
import com.example.ApiUser.entity.Permission;
import com.example.ApiUser.entity.User;
import com.example.ApiUser.enums.Role;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.PermissionMapper;
import com.example.ApiUser.mapper.UserMapper;
import com.example.ApiUser.respository.PermissionRepository;
import com.example.ApiUser.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
//        var permissions = permissionRepository.findAll();
//        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
        return permissionMapper.toListPermission(permissionRepository.findAll());
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
