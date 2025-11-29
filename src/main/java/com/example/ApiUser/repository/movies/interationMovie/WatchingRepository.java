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
import java.util.Optional;

public interface WatchingRepository extends JpaRepository<Watching, String> {
    List<Watching> findAllByUser(User user);

    List<Watching> findAllByMovie(Movie movie);

    boolean existsByUserAndMovieAndDataMovie(User user, Movie movie, DataMovie dataMovie);

    Optional<Watching> findByUserAndDataMovie(User user, DataMovie dataMovie);

    Optional<Watching> findByUser(User user);

    boolean existsByUserAndDataMovie(User user, DataMovie dataMovie);


    int deleteByLastWatchedAtBefore(LocalDateTime thresholdDate);

    @Query("""
                SELECT new com.example.ApiUser.dto.response.admin.CountMovie(
                    w.movie.id, 
                    w.movie.name,
                    w.movie.slug,
                    COUNT(DISTINCT w.user.id)
                )
                FROM Watching w
                GROUP BY w.movie.id, w.movie.name, w.movie.slug
                ORDER BY COUNT(DISTINCT w.user.id) DESC
            """)
    List<CountMovie> findMovieWatchCounts();

}

