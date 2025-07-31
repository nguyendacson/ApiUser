package com.example.ApiUser.service;

import com.example.ApiUser.dto.request.UserCreationRequest;
import com.example.ApiUser.dto.request.UserUpdateRequest;
import com.example.ApiUser.dto.response.UserResponse;
import com.example.ApiUser.entity.User;
import com.example.ApiUser.enums.Role;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.UserMapper;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername(userCreationRequest.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(userCreationRequest);

        HashSet<String > roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUser(){
        return userMapper.toListUserResponse(userRepository.findAll());
    }

//    @PostAuthorize("returnObject.username == authentication.username")
//    @PreAuthorize("hasRole('ADMIN')")
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        userMapper.updateUser(user,userUpdateRequest);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id){
          userRepository.deleteById(id);
    }
}
