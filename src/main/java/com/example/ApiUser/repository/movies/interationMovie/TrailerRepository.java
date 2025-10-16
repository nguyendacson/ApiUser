package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Trailer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrailerRepository extends JpaRepository<Trailer, String> {
    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<Trailer> findByUserAndMovie(User user, Movie movie);

    List<Trailer> findAllByUser(User user, Sort sort);

    List<Trailer> findAllByUser(User user);
}
