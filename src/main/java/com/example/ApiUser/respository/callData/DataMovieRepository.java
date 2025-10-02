package com.example.ApiUser.respository.callData;

import com.example.ApiUser.entity.movies.DataMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DataMovieRepository extends JpaRepository<DataMovie, String> {

    @Query("SELECT dm.name FROM DataMovie dm WHERE dm.name IN :names AND dm.episode.movie.id = :movieId")
    Set<String> findExistingName(@Param("names") Set<String> names, @Param("movieId") String movieId);

    Optional<DataMovie> findByName(String name); // Thêm dòng này
}
