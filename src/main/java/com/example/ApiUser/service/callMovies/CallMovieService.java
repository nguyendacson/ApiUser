package com.example.ApiUser.service.callMovies;

import com.example.ApiUser.dto.response.callMovies.ApiMovieResponse;
import com.example.ApiUser.dto.response.callMovies.DataMovieResponse;
import com.example.ApiUser.dto.response.callMovies.EpisodeResponse;
import com.example.ApiUser.dto.response.callMovies.MovieCallResponse;
import com.example.ApiUser.entity.callMovies.*;
import com.example.ApiUser.mapper.callMovie.MovieMapper;
import com.example.ApiUser.mapper.callMovie.CategoryMapper;
import com.example.ApiUser.mapper.callMovie.CountryMapper;
import com.example.ApiUser.mapper.callMovie.DataMovieMapper;
import com.example.ApiUser.mapper.callMovie.EpisodeMapper;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.callMovies.*;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CallMovieService {
    UserRepository userRepository;
    CountryRepository countryRepository;
    CountryMapper countryMapper;
    MovieCallRepository movieRepository;
    MovieMapper movieMapper;
    MovieDTOMapper movieDTOMapper;
    ActorRepository actorRepository;
    DirectorRepository directorRepository;
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    EpisodeRepository episodeRepository;
    EpisodeMapper episodeMapper;
    DataMovieRepository dataMovieRepository;
    DataMovieMapper dataMovieMapper;
    RestTemplate restTemplate;

    private <D, T> Set<T> mapData(List<D> data, Function<D, Optional<T>> findFunc, Function<D, T> createAndSaveFunc) {

        if (data == null || data.isEmpty()) {
            return Collections.emptySet();
        }

        return data.stream()
//                .filter(item -> item != null && !item.toString().trim().isEmpty())  // Filter null/empty (adjust cho D=String)
                .map(item -> findFunc.apply(item)
                        .orElseGet(() -> createAndSaveFunc.apply(item)))
                .collect(Collectors.toSet());
    }

    public Movie mapdataMovie(MovieCallResponse movieResponse) {
        Movie movie = movieMapper.toMovie(movieResponse);

        movie.setCreated(movieResponse.getCreated().getTime());

        movie.setModified(movieResponse.getModified().getTime());

        movie.setActors(mapData(
                movieResponse.getActors(),
                actorRepository::findByName,
                dataName -> actorRepository.save(Actor.builder().name(dataName).build())));


        movie.setDirectors(mapData(
                movieResponse.getDirectors(),
                directorRepository::findByName,
                dataName -> directorRepository.save(Director.builder().name(dataName).build())
        ));

        movie.setCategories(mapData(
                movieResponse.getCategories(),
                categoryResponse -> categoryRepository.findById(categoryResponse.getId()),
                categoryResponse -> categoryRepository.save(categoryMapper.toEntity(categoryResponse))
        ));

        movie.setCountries(mapData(
                movieResponse.getCountries(),
                countryResponse -> countryRepository.findById(countryResponse.getId()),
                countryResponse -> countryRepository.save(countryMapper.toEntity(countryResponse))
        ));

        return movie;
    }

    public MovieCallResponse newData(String apiUrl) {
        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);

        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
            throw new RuntimeException("API không trả dữ liệu movie");
        }

        MovieCallResponse movieResponse = apiMovieResponse.getMovie();

        Movie movie = mapdataMovie(movieResponse);

        Movie saveData = movieRepository.save(movie);

        Movie finalSaveData = saveData;

        List<Episode> episodes = apiMovieResponse.getEpisodeResponseList().stream()
                .map(epResp -> createEpisodeWithDataMovies(epResp, finalSaveData))
                .toList();

        saveData.getEpisodes().addAll(episodes);
        saveData = movieRepository.save(saveData);

        return movieMapper.toMovieResponse(saveData);
    }

    public MovieCallResponse updateData(String apiUrl) {
        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);

        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
            throw new RuntimeException("API không trả dữ liệu movie");
        }

        MovieCallResponse movieResponse = apiMovieResponse.getMovie();

        Optional<Movie> optionalMovie = movieRepository.findById(movieResponse.getId());

        Movie saveData = optionalMovie.orElseThrow(() ->
                new RuntimeException("Movie not found with id: " + movieResponse.getId()));

        saveData.setStatus(movieResponse.getStatus());
        saveData.setEpisode_current(movieResponse.getEpisode_current());
        saveData.setYear(movieResponse.getYear());

        saveData = movieRepository.save(saveData);

        Movie finalSaveData = saveData;

        List<Episode> episodes = apiMovieResponse.getEpisodeResponseList().stream()
                .map(epResp -> updateEpisodeWithDataMovies(epResp, finalSaveData))
                .toList();

        saveData.getEpisodes().addAll(episodes);
        saveData = movieRepository.save(saveData);

        return movieMapper.toMovieResponse(saveData);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMovie(String keySearch) {
        if (keySearch == null || keySearch.trim().isEmpty()) {
            throw new IllegalArgumentException("key Search not null or empty!!!");
        }

        Movie movie = movieRepository.findByName(keySearch)
                .or(() -> movieRepository.findBySlug(keySearch.trim()))
                .orElseThrow(() -> new RuntimeException("Not found movie with name or slug"));

        movieRepository.delete(movie);
    }

    private Episode createEpisodeWithDataMovies(EpisodeResponse epResp, Movie movie) {
        Episode ep;
        ep = episodeMapper.toEntity(epResp);
        ep.setMovie(movie);
        ep.setDataMovies(new HashSet<>());
        ep = episodeRepository.save(ep);

        Episode finalEp = ep;
        Set<DataMovie> newDataMovies = epResp.getDataMovies().stream()
                .map(dataMovieMapper::toEntity)
                .peek(dm -> dm.setEpisode(finalEp))  // ep.id có sẵn → episode_id sẽ != null khi insert
                .collect(Collectors.toSet());

        ep.getDataMovies().addAll(newDataMovies);
        return episodeRepository.save(ep);  // Cascade persist DataMovie với episode_id đúng
    }


    private Episode updateEpisodeWithDataMovies(EpisodeResponse epResp, Movie movie) {
        Optional<Episode> optionalEpisode = episodeRepository.findByMovieIdAndServerName(movie.getId(), epResp.getServerName());
        Episode ep;
        if (optionalEpisode.isPresent()) {
            ep = optionalEpisode.get();
            ep.setServerName(epResp.getServerName());
        } else {
            ep = episodeMapper.toEntity(epResp);
            ep.setMovie(movie);

            ep.setDataMovies(new HashSet<>());
            ep = episodeRepository.save(ep);
        }

        Set<String> inputMovieDataNames = epResp.getDataMovies()
                .stream().map(DataMovieResponse::getName)
                .collect(Collectors.toSet());

        Set<String> existingNames = dataMovieRepository.findExistingName(inputMovieDataNames, ep.getId());

        Episode finalEp = ep;
        Set<DataMovie> updateDataMovies = epResp.getDataMovies().stream()
                .filter(dmResp -> !existingNames.contains(dmResp.getName()))
                .map(dataMovieMapper::toEntity)
                .peek(dm -> dm.setEpisode(finalEp))
                .collect(Collectors.toSet());

        ep.getDataMovies().addAll(updateDataMovies);

        return episodeRepository.save(ep);
    }

}
