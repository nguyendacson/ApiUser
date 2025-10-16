package com.example.ApiUser.dto.response.callMovies;

import com.example.ApiUser.entity.callMovies.Created;
import com.example.ApiUser.entity.callMovies.Modified;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieCallResponse {
    @JsonProperty("_id")
    String id;
    String name;
    String slug;
    String origin_name;
    Created created;
    Modified modified;
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
    String year;

    @JsonProperty("actor")
    List<String> actors;

    @JsonProperty("director")
    List<String> directors;

    @JsonProperty("category")
    List<CategoryResponse> categories;

    @JsonProperty("country")
    List<CountryResponse> countries;

    List<EpisodeResponse> episodes;
}
