package com.example.ApiUser.repository.authentication;

import com.example.ApiUser.entity.authentication.token.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
