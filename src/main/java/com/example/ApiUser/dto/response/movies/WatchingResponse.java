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
    String id;
    Integer progressSeconds;
    LocalDateTime lastWatchedAt;
    String dataMovieId;
    String dataMovieName;
    MovieDTO movieDTO;
}
