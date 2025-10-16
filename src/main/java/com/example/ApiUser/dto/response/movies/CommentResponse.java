package com.example.ApiUser.dto.response.movies;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String user;
    String movie;
    String content;
    boolean owner;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
