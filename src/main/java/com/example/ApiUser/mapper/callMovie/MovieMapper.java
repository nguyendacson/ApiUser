package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.dto.response.callMovies.MovieCallResponse;
import com.example.ApiUser.entity.callMovies.Movie;
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
    Movie toMovie(MovieCallResponse movieResponse);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "episodes", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    MovieCallResponse toMovieResponse(Movie movie);
}
