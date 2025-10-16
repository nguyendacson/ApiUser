package com.example.ApiUser.repository.callMovies;

import com.example.ApiUser.entity.callMovies.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, String> {
    Optional<Episode> findByMovieIdAndServerName(String movieId, String serverName);
}
