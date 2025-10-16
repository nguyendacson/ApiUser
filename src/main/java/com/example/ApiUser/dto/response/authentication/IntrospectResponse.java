package com.example.ApiUser.dto.response.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class IntrospectResponse {
    boolean valid;
}
