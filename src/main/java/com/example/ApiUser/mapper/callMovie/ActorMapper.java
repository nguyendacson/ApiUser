package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.entity.callMovies.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    @Named("actorsToStrings")  // Qualified method để convert
    default List<String> actorsToStrings(List<Actor> actors) {
        if (actors == null) return null;
        return actors.stream()
                .map(Actor::getName)
                .collect(Collectors.toList());
    }
}
