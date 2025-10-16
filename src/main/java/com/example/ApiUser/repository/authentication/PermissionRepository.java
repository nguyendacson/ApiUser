package com.example.ApiUser.repository.authentication;

import com.example.ApiUser.entity.authentication.token.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
