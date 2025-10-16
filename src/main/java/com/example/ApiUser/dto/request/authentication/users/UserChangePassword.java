package com.example.ApiUser.dto.request.authentication.users;


import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangePassword {
    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;

    @Size(min = 8,message = "PASSWORD_INVALID")
    String newPassword;
}
