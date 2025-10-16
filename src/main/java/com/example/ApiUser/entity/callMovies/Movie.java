package com.example.ApiUser.entity.callMovies;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {
    @Id
    @JsonProperty("_id")
    String id;
    String name;
    String slug;

    @JsonProperty("origin_name")
    @Column(name = "origin_name")
    String originName;

    @Column(nullable = false)
    Instant created;
    @Column(nullable = false)
    Instant modified;
    @Lob
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
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Actor> actors = new HashSet<>();

    @ManyToMany
    @JsonProperty("category")
    Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JsonProperty("country")
    Set<Country> countries = new HashSet<>();

    @ManyToMany
    Set<Director> directors = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Episode> episodes = new HashSet<>();

}
