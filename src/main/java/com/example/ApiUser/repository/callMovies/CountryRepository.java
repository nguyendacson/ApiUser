package com.example.ApiUser.repository.callMovies;

import com.example.ApiUser.entity.callMovies.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
