package com.example.ApiUser.respository.callData;

import com.example.ApiUser.entity.movies.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, String> {
    Optional<Actor> findByName(String name);
}
