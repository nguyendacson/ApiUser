package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.dto.response.admin.CountMovie;
import com.example.ApiUser.entity.callMovies.DataMovie;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Watching;
import com.example.ApiUser.entity.authentication.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WatchingRepository extends JpaRepository<Watching, String> {
    List<Watching> findAllByUser(User user);

    boolean existsByUserAndMovieAndDataMovie(User user, Movie movie, DataMovie dataMovie);

    int deleteByLastWatchedAtBefore(LocalDateTime thresholdDate);

    @Query("""
                SELECT new com.example.ApiUser.dto.response.admin.CountMovie(
                    w.movie.id, COUNT(DISTINCT w.user.id)
                )
                FROM Watching w
                GROUP BY w.movie.id
                ORDER BY COUNT(DISTINCT w.user.id) DESC
            """)
    List<CountMovie> findMovieWatchCounts();

}

