package com.example.ApiUser.dto.request.movies;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WatchingRequest {
    String movieId;
    String dataMovieId;
    Integer progressSeconds;
    LocalDateTime lastWatchedAt;

}
