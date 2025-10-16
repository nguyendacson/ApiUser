package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.entity.callMovies.DataMovie;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Watching;
import com.example.ApiUser.entity.authentication.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WatchingRepository extends JpaRepository<Watching, String> {
    List<Watching> findByUserId(String userId);
    List<Watching> findAllByUser(User user);

    boolean existsByUser_Id(String userId);

    boolean existsByUserAndMovieAndDataMovie(User user, Movie movie, DataMovie dataMovie);

    //auto delete after six month
    int deleteByLastWatchedAtBefore(LocalDateTime thresholdDate);
}

