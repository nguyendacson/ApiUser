package com.example.ApiUser.service.helper;

import com.example.ApiUser.dto.request.movies.MovieFilterRequest;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.service.movies.movieService.MovieSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BuildSpecificationHelper {
    public Specification<Movie> buildSpecification(MovieFilterRequest filter) {
        Specification<Movie> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getType() != null) spec = spec.and(MovieSpecifications.hasType(filter.getType()));
        if (filter.getStatus() != null) spec = spec.and(MovieSpecifications.hasStatus(filter.getStatus()));
        if (filter.getLang() != null) spec = spec.and(MovieSpecifications.hasLang(filter.getLang()));
        if (filter.getCategory() != null) spec = spec.and(MovieSpecifications.hasCategory(filter.getCategory()));
        if (filter.getCountry() != null) spec = spec.and(MovieSpecifications.hasCountry(filter.getCountry()));
        if (filter.getYear() != null) spec = spec.and(MovieSpecifications.hasYear(filter.getYear()));

        return spec;
    }
}
