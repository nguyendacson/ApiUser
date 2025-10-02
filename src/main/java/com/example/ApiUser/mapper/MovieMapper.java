package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.response.callData.MovieResponse;
import com.example.ApiUser.entity.movies.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "episodes", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    Movie toMovie(MovieResponse movieResponse);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "episodes", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    MovieResponse toMovieResponse(Movie movie);
}
