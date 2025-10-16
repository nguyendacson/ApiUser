package com.example.ApiUser.dto.response.callMovies;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryResponse {
    String id;
    String name;
    String slug;
}
