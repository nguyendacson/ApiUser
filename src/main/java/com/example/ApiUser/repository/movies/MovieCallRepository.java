package com.example.ApiUser.repository.movies;

import com.example.ApiUser.entity.callMovies.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieCallRepository extends JpaRepository<Movie, String>, JpaSpecificationExecutor<Movie> {
    // Lấy tất cả slug có trong danh sách và đang ongoing
    @Query("SELECT DISTINCT m.slug FROM Movie m WHERE m.slug IN :slugs AND LOWER(m.status) = LOWER(:status)")
    Set<String> findBySlugInAndStatusIgnoreCase(@Param("slugs") Set<String> slugs, @Param("status") String status);

//    Optional<Movie> findBySlug(String slug);

    Optional<Movie> findBySlug(String slug);

    @Query("SELECT m.slug FROM Movie m")
    Set<String> findAllSlugs();

    List<Movie> findTop50ByYearAndStatusOrderByCreatedDesc(String year, String status);

    @Query(value = """
        SELECT m.id 
        FROM Movie m 
        WHERE m.year = :year AND m.status = :status
        ORDER BY m.created DESC
        LIMIT 50
        """, nativeQuery = true)
    List<String> findTop50IdsByYearAndStatus(@Param("year") String year, @Param("status") String status);

    @Query("""
        SELECT m.id 
        FROM Movie m 
        JOIN m.categories c 
        WHERE m.type = :type 
          AND c.slug = :categorySlug 
        ORDER BY m.created DESC
        """)
    List<String> findIdsByTypeAndCategorySlug(
            @Param("type") String type,
            @Param("categorySlug") String categorySlug,
            Pageable pageable);

    // trả tối đa 10 kết quả bắt đầu bằng prefix (ignore case)
    List<Movie> findTop10ByOriginNameStartingWithIgnoreCase(String namePrefix);

    List<Movie> findTop10BySlugStartingWithIgnoreCase(String prefix);


}
