package com.example.ApiUser.dto.request.movies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WatchingCreateRequest {


    String userId;
    String movieId;
    String dataMovieId;
    Integer progressSeconds;
    LocalDateTime lastWatchedAt;

//    @JsonProperty("user_id")
//    String userId;
//
//    @JsonProperty("movie_id")
//    String movieId;
//
//    @JsonProperty("data_movie_id")
//    String dataMovieId;
//
//    @JsonProperty("progress_seconds")
//    Integer progressSeconds;
//
//    @JsonProperty("last_watched_at")
//    LocalDateTime lastWatchedAt;
}
