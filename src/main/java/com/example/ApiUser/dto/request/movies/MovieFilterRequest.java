package com.example.ApiUser.dto.request.movies;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieFilterRequest {
    Integer page;
    Integer limit;
    String status;
    String type;
    String sortField;
    String sortType;
    String lang;
    String category;
    String country;
    Integer year;
}
