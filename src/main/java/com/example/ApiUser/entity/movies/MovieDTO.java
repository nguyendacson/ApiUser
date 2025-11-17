package com.example.ApiUser.entity.movies;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieDTO {
    String id;
    String name;
    String slug;
    String type;
    String origin_name;
    String poster_url;
    String thumb_url;
    String episode_current;
    String episode_total;
    String time;
    String content;
    String year;
    String quality;

    Set<DirectorDTO> directors;
}
