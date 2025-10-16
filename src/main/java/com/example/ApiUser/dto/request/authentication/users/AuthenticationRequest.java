package com.example.ApiUser.dto.request.authentication.users;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @Size(min = 8, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;
    String email;
}
