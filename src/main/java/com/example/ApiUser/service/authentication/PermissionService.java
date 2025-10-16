package com.example.ApiUser.service.authentication;

import com.example.ApiUser.dto.request.authentication.token.PermissionRequest;
import com.example.ApiUser.dto.response.authentication.PermissionResponse;
import com.example.ApiUser.entity.authentication.token.Permission;
import com.example.ApiUser.mapper.authentication.PermissionMapper;
import com.example.ApiUser.repository.authentication.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
