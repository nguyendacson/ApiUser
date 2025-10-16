package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.dto.response.callMovies.EpisodeResponse;
import com.example.ApiUser.entity.callMovies.Episode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DataMovieMapper.class})
public interface EpisodeMapper {
    Episode toEntity(EpisodeResponse dto);

    EpisodeResponse toResponse(Episode dto);
}
