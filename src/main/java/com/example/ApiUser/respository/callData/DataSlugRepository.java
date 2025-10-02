package com.example.ApiUser.respository.callData;

import com.example.ApiUser.entity.movies.DataSlug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DataSlugRepository extends JpaRepository<DataSlug, String> {
}
