package com.example.ApiUser.service.movies;

import com.example.ApiUser.dto.request.movies.WatchingCreateRequest;
import com.example.ApiUser.dto.response.callData.ApiMovieResponse;
import com.example.ApiUser.dto.response.callData.DataMovieResponse;
import com.example.ApiUser.dto.response.callData.EpisodeResponse;
import com.example.ApiUser.dto.response.callData.MovieResponse;
import com.example.ApiUser.dto.response.movies.WatchingResponse;
import com.example.ApiUser.entity.movies.*;
import com.example.ApiUser.entity.relationships.WatchingList;
import com.example.ApiUser.entity.user.User;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.*;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.respository.UserRepository;
import com.example.ApiUser.respository.callData.*;
import com.example.ApiUser.respository.movies.WatchingListRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MovieService {
    UserRepository userRepository;
    CountryRepository countryRepository;
    CountryMapper countryMapper;
    MovieRepository movieRepository;
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
    WatchingListRepository watchingListRepository;
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

    public Movie mapdataMovie(MovieResponse movieResponse) {
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

    public MovieResponse newData(String apiUrl) {
        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);

        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
            throw new RuntimeException("API không trả dữ liệu movie");
        }

        MovieResponse movieResponse = apiMovieResponse.getMovie();

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

    public MovieResponse updateData(String apiUrl) {
        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);

        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
            throw new RuntimeException("API không trả dữ liệu movie");
        }

        MovieResponse movieResponse = apiMovieResponse.getMovie();

        Optional<Movie> optionalMovie = movieRepository.findById(movieResponse.getId());

        Movie saveData = optionalMovie.orElseThrow(() ->
                new RuntimeException("Movie not found with id: " + movieResponse.getId()));

        saveData.setStatus(movieResponse.getStatus());
        saveData.setEpisode_current(movieResponse.getEpisode_current());

        saveData = movieRepository.save(saveData);

        Movie finalSaveData = saveData;

        List<Episode> episodes = apiMovieResponse.getEpisodeResponseList().stream()
                .map(epResp -> updateEpisodeWithDataMovies(epResp, finalSaveData))
                .toList();

        saveData.getEpisodes().addAll(episodes);
        saveData = movieRepository.save(saveData);

        return movieMapper.toMovieResponse(saveData);
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

    public WatchingResponse createWatchingList(WatchingCreateRequest watchingCreateRequest) {
        String getIdUser = watchingCreateRequest.getUserId();
        String getIdMovie = watchingCreateRequest.getMovieId();
        String getIdDataMovie = watchingCreateRequest.getDataMovieId();

        if (watchingListRepository.existsByUserAndMovieAndDataMovie(getIdUser, getIdMovie, getIdDataMovie)) {
            throw new AppException(ErrorCode.USER_DATA_MOVIE_NOT_EXISTED);
        }

        User user = userRepository.findById(getIdUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Movie movie = movieRepository.findById(getIdMovie).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));

        DataMovie dataMovie = dataMovieRepository.findById(getIdDataMovie).orElseThrow(() -> new AppException(ErrorCode.DATA_MOVIE_NOT_EXISTED));

        WatchingList watchingList = WatchingList.builder().user(user).movie(movie).dataMovie(dataMovie).progressSeconds(watchingCreateRequest.getProgressSeconds()).lastWatchedAt(watchingCreateRequest.getLastWatchedAt() != null ? watchingCreateRequest.getLastWatchedAt() : LocalDateTime.now()).build();

        WatchingList saved = watchingListRepository.save(watchingList);

        return WatchingResponse.builder().progressSeconds(saved.getProgressSeconds()).lastWatchedAt(saved.getLastWatchedAt()).movieDTO(movieDTOMapper.toDTO(saved.getMovie())).build();
    }
}
