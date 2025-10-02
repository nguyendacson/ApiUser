package com.example.ApiUser.mapper;

import com.example.ApiUser.dto.response.callData.CategoryResponse;
import com.example.ApiUser.entity.movies.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryResponse dto);
}
