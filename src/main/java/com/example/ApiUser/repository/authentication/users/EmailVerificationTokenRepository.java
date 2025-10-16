package com.example.ApiUser.repository.authentication.users;

import com.example.ApiUser.entity.authentication.users.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {
//    List<CloudinaryCleanupRepository> findAllByDeletedFalse();
    Optional<EmailVerificationToken> findByToken(String token);

    int deleteByExpiryDateBefore(LocalDateTime dateTime);
}

