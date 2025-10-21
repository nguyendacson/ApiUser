package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.dto.response.admin.CountMovie;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Likes;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, String> {
    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<Likes> findByUserAndMovie(User user, Movie movie);

    List<Likes> findAllByUser(User user, Sort sort);

    List<Likes> findAllByUser(User user);

    @Query("""
                SELECT new com.example.ApiUser.dto.response.admin.CountMovie(
                    l.movie.id, COUNT(DISTINCT l.user.id)
                )
                FROM Likes l
                GROUP BY l.movie.id
                ORDER BY COUNT(DISTINCT l.user.id) DESC
            """)
    List<CountMovie> findMovieListCounts();


}
