package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.dto.response.callMovies.CountryResponse;
import com.example.ApiUser.entity.callMovies.Country;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country toEntity(CountryResponse countryResponse);

    List<CountryResponse> toCountryResponse(List<Country> country);
}
