package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.response.callData.EpisodeResponse;
import com.example.ApiUser.entity.movies.Episode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DataMovieMapper.class})
public interface EpisodeMapper {
    Episode toEntity(EpisodeResponse dto);
}
