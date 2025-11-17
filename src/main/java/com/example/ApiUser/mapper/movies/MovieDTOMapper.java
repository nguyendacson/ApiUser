package com.example.ApiUser.mapper.movies;

import com.example.ApiUser.entity.callMovies.Director;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.DirectorDTO;
import com.example.ApiUser.entity.movies.MovieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieDTOMapper {
    @Mapping(source = "originName", target = "origin_name")
    MovieDTO toDTO(Movie movie);

    default DirectorDTO toDirectorDTO(Director director) {
        if (director == null) return null;
        return DirectorDTO.builder()
                .name(director.getName())
                .build();
    }
}
