package com.example.ApiUser.dto.response.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String name;
    String username;
    String avatar;
    String email;
    LocalDate dob;
    Set<RoleResponse> roles;
    boolean emailVerified;
    String provider;
}
