package com.example.ApiUser.dto.request.movies.comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
    String commentId;
    String newContent;
}
