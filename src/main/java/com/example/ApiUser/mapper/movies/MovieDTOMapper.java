package com.example.ApiUser.mapper.movies;

import com.example.ApiUser.entity.movies.Movie;
import com.example.ApiUser.entity.movies.MovieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieDTOMapper {

    // map từ Movie entity -> MovieDTO
    MovieDTO toDTO(Movie movie);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "trailer_url", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "lang", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "episodes", ignore = true)
    Movie toEntity(MovieDTO dto);
}
