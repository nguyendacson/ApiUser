package com.example.ApiUser.service.movies.movieService;

import com.example.ApiUser.dto.request.movies.MovieFilterRequest;
import com.example.ApiUser.dto.response.callMovies.CategoryResponse;
import com.example.ApiUser.dto.response.callMovies.CountryResponse;
import com.example.ApiUser.dto.response.callMovies.EpisodeResponse;
import com.example.ApiUser.entity.callMovies.*;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.callMovie.*;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.callMovies.CategoryRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.service.helper.BuildSpecificationHelper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MovieService {
    MovieCallRepository movieCallRepository;
    CategoryRepository categoryRepository;
    MovieMapper movieMapper;
    CategoryMapper categoryMapper;
    ActorMapper actorMapper;
    DirectorMapper directorMapper;
    CountryMapper countryMapper;
    EpisodeMapper episodeMapper;
    MovieDTOMapper movieDTOMapper;
    DataMovieMapper dataMovieMapper;
    BuildSpecificationHelper buildSpecificationHelper;

    public List<EpisodeResponse> episodeByMovie(String movieId) {
        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        return movie.getEpisodes().stream()
                .map(episode -> {
                    // ✅ Log tất cả DataMovie trong episode
                    episode.getDataMovies().forEach(dm ->
                            System.out.println(">>> ENTITY DataMovie ID = " + dm.getId()
                                    + " | name = " + dm.getName())
                    );

                    return EpisodeResponse.builder()
                            .serverName(episode.getServerName())
                            .dataMovies(
                                    episode.getDataMovies().stream()
                                            .sorted(Comparator.comparing(DataMovie::getName, Comparator.nullsLast(String::compareTo)))
                                            .map(dataMovieMapper::toResponse)
                                            .toList()
                            )
                            .build();
                })
                .toList();
    }


    public MovieDTO getMovieById(String id) {
        Movie movie = movieCallRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));
        return movieDTOMapper.toDTO(movie);
    }

    private String capitalizeWords(String keySearch) {
        if (keySearch == null || keySearch.isEmpty()) return "";
        return Arrays.stream(keySearch.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(w -> w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private String toSlug(String keySearch) {
        if (keySearch == null || keySearch.isEmpty()) return "";
        return Arrays.stream(keySearch.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.joining("-"));
    }

    @PreAuthorize("isAuthenticated()")
    public List<MovieDTO> searchMovie(String keySearch) {
        List<Movie> movies = movieCallRepository.findTop10ByOriginNameStartingWithIgnoreCase(capitalizeWords(keySearch));
        List<Movie> movies1 = movieCallRepository.findTop10BySlugStartingWithIgnoreCase(toSlug(keySearch));

        log.info("List movies{}", movies);
        log.info("List movies1{}", movies1);
        Set<Movie> result = new LinkedHashSet<>();
        result.addAll(movies);
        result.addAll(movies1);

        return result.stream()
                .map(movieDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated")
    public List<CategoryResponse> allCategory() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toEntityResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated")
    public List<MovieDTO> allMovieByFilter(MovieFilterRequest filterRequest, Pageable pageable) {
        Page<Movie> page = movieCallRepository.findAll(
                buildSpecificationHelper.buildSpecification(filterRequest),
                pageable
        );

        log.info("DATA tacgia: {}", page.map(Movie::getDirectors));
        page.getContent().forEach(movie ->
                log.info("Movie: {}, Directors: {}",
                        movie.getName(),
                        movie.getDirectors().stream()
                                .map(Director::getName)
                                .toList()
                )
        );


        return page.getContent().stream()
                .map(movieDTOMapper::toDTO)
                .toList();
    }

    @PreAuthorize("isAuthenticated")
    public List<String> actorByMovie(String movieId) {
        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));
        List<Actor> list = new ArrayList<>(movie.getActors());
        return actorMapper.actorsToStrings(list);
    }

    @PreAuthorize("isAuthenticated")
    public List<String> directorByMovie(String movieId) {
        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));
        List<Director> list = new ArrayList<>(movie.getDirectors());
        return directorMapper.directorsToStrings(list);
    }

    @PreAuthorize("isAuthenticated")
    public List<CountryResponse> countryByMovie(String movieId) {
        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));
        List<Country> list = new ArrayList<>(movie.getCountries());
        return countryMapper.toCountryResponse(list);
    }


}
