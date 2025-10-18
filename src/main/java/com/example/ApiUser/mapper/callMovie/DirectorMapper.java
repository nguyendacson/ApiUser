package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.entity.callMovies.Director;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DirectorMapper {

    @Named("directorsToStrings")
    default List<String> directorsToStrings(List<Director> actors) {
        if (actors == null) return null;
        return actors.stream()
                .map(Director::getName)
                .collect(Collectors.toList());
    }
}
