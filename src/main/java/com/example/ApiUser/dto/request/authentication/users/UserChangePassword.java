package com.example.ApiUser.dto.request.authentication.users;


import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangePassword {
    @Size(min = 8,message = "USR_PASSWORD_INVALID")
    String password;

    @Size(min = 8,message = "USR_PASSWORD_INVALID")
    String newPassword;
}
