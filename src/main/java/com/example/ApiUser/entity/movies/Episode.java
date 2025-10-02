package com.example.ApiUser.entity.movies;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String serverName;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    Movie movie;

    @OneToMany(mappedBy = "episode",cascade = CascadeType.ALL, orphanRemoval = true)
    Set<DataMovie> dataMovies = new HashSet<>();

}
