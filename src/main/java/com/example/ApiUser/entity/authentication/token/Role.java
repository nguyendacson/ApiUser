package com.example.ApiUser.entity.authentication.token;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    String name;
    String description;

    //    @ManyToMany
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Permission> permissions;
}
