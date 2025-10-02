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
public class EpisodeResponse {
    @JsonProperty("server_name")
    String serverName;

    @JsonProperty("server_data")
    List<DataMovieResponse> dataMovies;
}
