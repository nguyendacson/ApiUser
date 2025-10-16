package com.example.ApiUser.repository.callMovies;

import com.example.ApiUser.entity.callMovies.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
