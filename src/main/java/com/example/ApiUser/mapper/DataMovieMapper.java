package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.response.callData.DataMovieResponse;
import com.example.ApiUser.entity.movies.DataMovie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataMovieMapper {
    DataMovie toEntity(DataMovieResponse dto);
}
