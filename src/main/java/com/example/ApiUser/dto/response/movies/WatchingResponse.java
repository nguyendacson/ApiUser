package com.example.ApiUser.dto.response.movies;

import com.example.ApiUser.entity.movies.MovieDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WatchingResponse {
    Integer progressSeconds;
    LocalDateTime lastWatchedAt;
    String dataMovieId;
    MovieDTO movieDTO;
}
