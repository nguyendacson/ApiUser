package com.example.ApiUser.respository;

import com.example.ApiUser.entity.Permission;
import com.example.ApiUser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
