package com.example.ApiUser.dto.response.admin;

public record CountMovie(
        String movieId,
        String name,
        String slug,
        Long watchCount
) {
}
