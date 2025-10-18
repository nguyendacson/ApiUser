package com.example.ApiUser.controller.movies;

import com.example.ApiUser.dto.request.movies.MovieFilterRequest;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.dto.response.callMovies.CategoryResponse;
import com.example.ApiUser.dto.response.callMovies.CountryResponse;
import com.example.ApiUser.dto.response.callMovies.EpisodeResponse;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.service.helper.PaginationHelper;
import com.example.ApiUser.service.movies.movieService.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/movies")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MovieController {
    MovieService movieService;
    PaginationHelper paginationHelper;

    @GetMapping("/{movie_Id}")
    ApiResponse<MovieDTO> getMovieById(@PathVariable String movie_Id) {
        return ApiResponse.<MovieDTO>builder()
                .result(movieService.getMovieById(movie_Id))
                .build();
    }


    @GetMapping("/categories")
    ApiResponse<List<CategoryResponse>> getAllCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(movieService.allCategory())
                .build();
    }

    @GetMapping("/filter/{type}")
    ApiResponse<List<MovieDTO>> getAllMovieByFilter(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "created") String sort_field,
            @RequestParam(defaultValue = "desc") String sort_type,
            @RequestParam(required = false) String sort_lang,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer year
    ) {
        Pageable pageable = paginationHelper.buildPageable(page, limit, sort_type, sort_field);

        MovieFilterRequest filterRequest = MovieFilterRequest.builder()
                .type(type)
                .status(status)
                .lang(sort_lang)
                .category(category)
                .country(country)
                .year(year)
                .page(page)
                .limit(limit)
                .sortField(sort_field)
                .sortType(sort_type)
                .build();

        List<MovieDTO> movies = movieService.allMovieByFilter(filterRequest, pageable);

        return ApiResponse.<List<MovieDTO>>builder()
                .result(movies)
                .build();
    }

    @PostMapping("/search")
    ApiResponse<List<MovieDTO>> searchMovie(@RequestParam(required = false) String key) {
        List<MovieDTO> list = movieService.searchMovie(key);
        return ApiResponse.<List<MovieDTO>>builder()
                .result(list)
                .build();
    }

    @PostMapping("/{movieId}/actor")
    ApiResponse<List<String>> actorByMovie(@PathVariable String movieId) {
        List<String> list = movieService.actorByMovie(movieId);
        return ApiResponse.<List<String>>builder()
                .result(list)
                .build();
    }

    @PostMapping("/{movieId}/country")
    ApiResponse<List<CountryResponse>> countryByMovie(@PathVariable String movieId) {
        List<CountryResponse> list = movieService.countryByMovie(movieId);
        return ApiResponse.<List<CountryResponse>>builder()
                .result(list)
                .build();
    }

    @PostMapping("/{movieId}/director")
    ApiResponse<List<String>> directorByMovie(@PathVariable String movieId) {
        List<String> list = movieService.directorByMovie(movieId);
        return ApiResponse.<List<String>>builder()
                .result(list)
                .build();
    }
    @PostMapping("/{movieId}/episode")
    ApiResponse<List<EpisodeResponse>> episodeByMovie(@PathVariable String movieId) {
        List<EpisodeResponse> list = movieService.episodeByMovie(movieId);
        return ApiResponse.<List<EpisodeResponse>>builder()
                .result(list)
                .build();
    }

}
