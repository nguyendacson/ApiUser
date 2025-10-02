package com.example.ApiUser.dto.request.waiting;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {
    String name;
    String slug;
    String origin_name;
    Instant created;
    Instant modified;
    String content;
    String type;
    String status;
    String poster_url;
    String thumb_url;
    String trailer_url;
    String time;
    String episode_current;
    String episode_total;
    String quality;
    String lang;

    Set<String> actors;
    Set<String> categories;
    Set<String> countries;
    Set<String> directors;
    Set<String> episodes;
}
