package com.example.ApiUser.respository.callData;

import com.example.ApiUser.entity.movies.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    // Lấy tất cả slug có trong danh sách và đang ongoing
    @Query("SELECT m.slug FROM Movie m WHERE m.slug IN :slugs AND LOWER(m.status) = 'ongoing'")
    Set<String> findOngoingSlugsIn(@Param("slugs") Set<String> slugs);
}
