package com.example.ApiUser.dto.response.callMovies.getSlug;

import lombok.Data;

@Data
public class MovieSlug {
    private String _id;
    private String slug;
    private String name;
    private String origin_name;
    private String poster_url;
    private String thumb_url;
    private int year;
}
