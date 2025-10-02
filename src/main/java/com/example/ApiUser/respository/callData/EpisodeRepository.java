package com.example.ApiUser.respository.callData;

import com.example.ApiUser.entity.movies.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, String> {
    Optional<Episode> findByMovieIdAndServerName(String movieId, String serverName);
}
