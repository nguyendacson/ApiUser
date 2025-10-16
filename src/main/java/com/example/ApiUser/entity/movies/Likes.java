package com.example.ApiUser.entity.movies;

import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.authentication.users.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "movie_id"})
        }
)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    Movie movie;

    @CreationTimestamp
    LocalDateTime createdAt;
}
