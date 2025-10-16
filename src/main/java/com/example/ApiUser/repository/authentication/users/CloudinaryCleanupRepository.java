package com.example.ApiUser.repository.authentication.users;

import com.example.ApiUser.entity.authentication.users.CloudinaryCleanup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CloudinaryCleanupRepository extends JpaRepository<CloudinaryCleanup, String> {
    List<CloudinaryCleanup> findAllByDeletedFalse();
}

