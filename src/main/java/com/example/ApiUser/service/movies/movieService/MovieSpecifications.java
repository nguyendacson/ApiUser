package com.example.ApiUser.service.movies.movieService;

import com.example.ApiUser.entity.callMovies.Movie;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class MovieSpecifications {
    public static Specification<Movie> hasType(String type) {
        return (root, query, cb) ->
                (type == null || type.isBlank()) ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Movie> hasStatus(String status) {
        return (root, query, cb) ->
                (status == null || status.isBlank()) ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Movie> hasCategory(String categorySlug) {
        return (root, query, cb) -> {
            if (categorySlug == null || categorySlug.isBlank()) return null;
            assert query != null;
            query.distinct(true);
            return cb.equal(root.join("categories").get("slug"), categorySlug);
        };
    }

    public static Specification<Movie> hasCountry(String countrySlug) {
        return (root, query, cb) -> {
            if (countrySlug == null || countrySlug.isBlank()) return null;
            assert query != null;
            query.distinct(true);
            return cb.equal(root.join("countries").get("slug"), countrySlug);
        };
    }

    public static Specification<Movie> hasLang(String lang) {
        return (root, query, cb) -> {
            if (lang == null || lang.isBlank()) return null;

            String normalized = normalize(lang);
            return cb.like(cb.lower(root.get("lang")), "%" + normalized + "%");
        };
    }


    public static Specification<Movie> hasYear(Integer year) {
        return (root, query, cb) ->
                (year == null) ? null : cb.equal(root.get("year"), String.valueOf(year));
    }

    private static String normalize(String input) {
        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfd).replaceAll("").toLowerCase(Locale.ROOT);
    }
}
