package com.example.ApiUser.mapper.authentication;

import com.example.ApiUser.dto.request.authentication.users.UserCreationRequest;
import com.example.ApiUser.dto.request.authentication.users.UserUpdateRequest;
import com.example.ApiUser.dto.response.authentication.UserResponse;
import com.example.ApiUser.dto.response.admin.UserResponseAdmin;
import com.example.ApiUser.entity.authentication.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);

    UserResponseAdmin toUserResponseAdmin(User user);

    List<UserResponseAdmin> toListUserResponseAdmin(List<User> userList);

    List<UserResponse> toListUserResponse(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
