package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Likes;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, String> {
    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<Likes> findByUserAndMovie(User user, Movie movie);

    List<Likes> findAllByUser(User user, Sort sort);

    List<Likes> findAllByUser(User user);
}
