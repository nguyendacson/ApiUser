package com.example.ApiUser.repository.authentication;

import com.example.ApiUser.entity.authentication.token.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
