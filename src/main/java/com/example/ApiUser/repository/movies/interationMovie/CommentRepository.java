package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Comment;
import com.example.ApiUser.entity.authentication.users.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String> {
    long countByUserAndMovieAndCreatedAtBetween(
            User user,
            Movie movie,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    Optional<Comment> findByUserAndMovie(User user, Movie movie);
    List<Comment> findAllByMovie(Movie movie, Sort sort);

}
