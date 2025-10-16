package com.example.ApiUser.repository.callMovies;

import com.example.ApiUser.entity.callMovies.DataMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DataMovieRepository extends JpaRepository<DataMovie, String> {
    @Query("SELECT dm.name FROM DataMovie dm WHERE dm.name IN :names AND dm.episode.id = :episodeId")
    Set<String> findExistingName(@Param("names") Set<String> names, @Param("episodeId") String episodeId);
}
