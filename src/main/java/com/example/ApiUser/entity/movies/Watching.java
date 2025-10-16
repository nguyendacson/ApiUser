package com.example.ApiUser.entity.movies;

import com.example.ApiUser.entity.callMovies.DataMovie;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.authentication.users.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "movie_id", "data_movie_id"})
        }
)
public class Watching {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "data_movie_id")
    DataMovie dataMovie;

    Integer progressSeconds;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime lastWatchedAt;
}
