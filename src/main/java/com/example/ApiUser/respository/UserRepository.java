package com.example.ApiUser.respository;

import com.example.ApiUser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByusername(String username);
//    Optional<User> findByusername(String username);
    Optional<User> findByusername(String username);
}
