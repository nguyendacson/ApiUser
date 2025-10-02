package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.response.callData.CountryResponse;
import com.example.ApiUser.entity.movies.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country toEntity(CountryResponse countryResponse);
}
