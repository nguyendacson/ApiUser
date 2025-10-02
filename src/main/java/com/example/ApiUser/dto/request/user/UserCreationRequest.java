package com.example.ApiUser.dto.request.user;

import com.example.ApiUser.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 8, message = "USERNAME_INVALID")
    String username;
    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;
    String email;

    @DobConstraint(min = 2, message = "INVALID_DOB")   // Only use when want to create new user and set age > 2 year
    LocalDate dob;
    List<String> roles;
}
