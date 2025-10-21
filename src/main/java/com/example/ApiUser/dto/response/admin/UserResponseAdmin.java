package com.example.ApiUser.dto.response.admin;

import com.example.ApiUser.entity.authentication.token.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseAdmin {
    String name;
    String username;
    String avatar;
    String email;
    LocalDate dob;
    boolean emailVerified;
    Set<Role> roles;
    String provider;
}
