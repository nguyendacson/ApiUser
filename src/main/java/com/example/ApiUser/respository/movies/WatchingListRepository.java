package com.example.ApiUser.respository.movies;

import com.example.ApiUser.entity.relationships.WatchingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchingListRepository extends JpaRepository<WatchingList, String> {
    List<WatchingList> findByUserId(String userId);
    boolean existsByUser_Id(String userId);

    @Query("SELECT COUNT(w) > 0 FROM WatchingList w " +
            "WHERE w.user.id = :userId AND w.movie.id = :movieId AND w.dataMovie.id = :dataMovieId")
    boolean existsByUserAndMovieAndDataMovie(@Param("userId") String userId,
                                             @Param("movieId") String movieId,
                                             @Param("dataMovieId") String dataMovieId);

}

