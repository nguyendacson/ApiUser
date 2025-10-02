package com.example.ApiUser.dto.response.callData;
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
    MovieResponse movie;
    @JsonProperty("episodes")
    List<EpisodeResponse> episodeResponseList;
}
