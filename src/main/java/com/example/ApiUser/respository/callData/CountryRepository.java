package com.example.ApiUser.respository.callData;

import com.example.ApiUser.entity.movies.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
