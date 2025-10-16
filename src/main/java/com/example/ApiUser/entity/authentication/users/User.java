package com.example.ApiUser.entity.authentication.users;
import com.example.ApiUser.entity.authentication.token.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String password;

    @Column(unique = true)
    String email;

    String avatar;
    String avatarPublicId;
    LocalDate dob;

    @ManyToMany
    Set<Role> roles;

    String provider;     // "local", "google", "facebook", ...
    String providerId;   // UID từ Firebase
    boolean emailVerified = false; // Để xác minh email
}
