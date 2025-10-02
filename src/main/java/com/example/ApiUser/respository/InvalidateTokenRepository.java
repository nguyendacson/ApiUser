package com.example.ApiUser.respository;

import com.example.ApiUser.entity.user.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
