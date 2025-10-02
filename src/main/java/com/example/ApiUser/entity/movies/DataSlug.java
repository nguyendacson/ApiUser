package com.example.ApiUser.entity.movies;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSlug {
    @Id
    String _id;
    String slug;
    String name;
    String origin_name;
    String poster_url;
    String thumb_url;
    int year;
}
