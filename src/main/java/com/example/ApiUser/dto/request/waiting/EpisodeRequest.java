package com.example.ApiUser.dto.request.waiting;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EpisodeRequest {
    String serverName;
    Set<String> dataMovies;
}
