package com.example.ApiUser.dto.response.callMovies;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataMovieResponse {
    String id;
    String name;
    String slug;
    String filename;
    String link_embed;
    String link_m3u8;
}
