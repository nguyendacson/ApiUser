package com.example.ApiUser.dto.response.callMovies;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiMovieResponse {
    boolean status;
    String msg;
    MovieCallResponse movie;
    @JsonProperty("episodes")
    List<EpisodeResponse> episodeResponseList;
}
