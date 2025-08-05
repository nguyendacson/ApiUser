package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.request.UserCreationRequest;
import com.example.ApiUser.dto.request.UserUpdateRequest;
import com.example.ApiUser.dto.response.UserResponse;
import com.example.ApiUser.entity.Role;
import com.example.ApiUser.entity.User;
import com.example.ApiUser.respository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles",ignore = true)
    User toUser(UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(User user);
    List<UserResponse> toListUserResponse(List<User> users);

    @Mapping(target = "roles",ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
