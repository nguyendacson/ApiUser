package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.dto.response.callMovies.DataMovieResponse;
import com.example.ApiUser.entity.callMovies.DataMovie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataMovieMapper {
    DataMovie toEntity(DataMovieResponse dto);
}
