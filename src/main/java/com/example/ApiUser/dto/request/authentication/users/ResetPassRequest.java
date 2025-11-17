package com.example.ApiUser.dto.request.authentication.users;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPassRequest {
    private String token;
    private String newPassword;
}
