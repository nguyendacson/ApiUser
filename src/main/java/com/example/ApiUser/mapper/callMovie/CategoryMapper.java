package com.example.ApiUser.mapper.callMovie;

import com.example.ApiUser.dto.response.callMovies.CategoryResponse;
import com.example.ApiUser.entity.callMovies.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryResponse dto);
    List<CategoryResponse> toEntityListResponse(List<Category> list);
    CategoryResponse toEntityResponse(Category list);
}
