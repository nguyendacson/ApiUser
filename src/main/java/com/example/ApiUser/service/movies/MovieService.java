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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
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


    private <D, T> Set<T> mapData(List<D> data, Function<D, Optional<T>> findFunc, Function<D, T> createEntityFunc, Consumer<T> saveFunc) {
        if (data == null || data.isEmpty()) {
            return Collections.emptySet();
        }
        return data.stream().map(item -> findFunc.apply(item).orElseGet(() -> {
            T entity = createEntityFunc.apply(item);
            saveFunc.accept(entity);
            return entity;
        })).collect(Collectors.toSet());
    }

//    public MovieResponse saveMovieFromApi(String apiUrl) {
//        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);
//
//        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
//            throw new RuntimeException("API không trả dữ liệu movie");
//        }
//
//        MovieResponse movieResponse = apiMovieResponse.getMovie();
//
//        List<EpisodeResponse> episodeResponses = apiMovieResponse.getEpisodeResponseList();
//
//        Movie movie = movieMapper.toMovie(movieResponse);
//
//        movie.setCreated(movieResponse.getCreated().getTime());
//        movie.setModified(movieResponse.getModified().getTime());
//
//        movie.setActors(mapData(
//                movieResponse.getActors(),
//                actorRepository::findByName,
//                dataName -> actorRepository.save(Actor.builder().name(dataName).build()),
//                actorRepository::save
//        ));
//
//        movie.setDirectors(mapData(
//                movieResponse.getDirectors(),
//                directorRepository::findByName,
//                dataName -> directorRepository.save(Director.builder().name(dataName).build()),
//                directorRepository::save
//        ));
//
//        movie.setCategories(mapData(
//                movieResponse.getCategories(),
//                data -> categoryRepository.findById(data.getId()),
//                categoryMapper::toEntity, categoryRepository::save
//        ));
//
//        movie.setCountries(mapData(
//                movieResponse.getCountries(),
//                data -> countryRepository.findById(data.getId()),
//                countryMapper::toEntity, countryRepository::save
//        ));
//
//        Movie saved = movieRepository.save(movie);
//
//        List<Episode> episodes =
//                apiMovieResponse.getEpisodeResponseList().stream().map(epResp -> createOrUpdateEpisodeWithDataMovies(epResp, saved)).toList();
//        /*
//               episodeResponses.stream().map(epResp -> createOrUpdateEpisodeWithDataMovies(epResp, saved)).toList();
//            saved.getEpisodes().clear();
//
//        */
//        saved.getEpisodes().addAll(episodes);
//        return movieMapper.toMovieResponse(movieRepository.save(saved));
//    }

//    private Episode createOrUpdateEpisodeWithDataMovies(EpisodeResponse epResp, Movie movie) {
//        // Tìm Episode theo movieId + episodeNumber
//        Optional<Episode> optionalEp = episodeRepository.findByMovieIdAndServerName(movie.getId(), epResp.getServerName());
//
//        Episode ep;
//        if (optionalEp.isPresent()) {
//            // Nếu Episode đã có trong DB → update
//            ep = optionalEp.get();

    /// /            ep.setName(epResp.getName()); // update thông tin khác nếu muốn
    /// /            ep.setModified(LocalDateTime.now());
//        } else {
//            // Nếu chưa có → tạo mới
//            ep = episodeMapper.toEntity(epResp);
//            ep.setMovie(movie);
//            ep = episodeRepository.save(ep); // save để có id
//        }
//
//        // Danh sách DataMovie name mới từ API
//        Set<String> inputMovieDataNames = epResp.getDataMovies().stream().map(DataMovieResponse::getName).collect(Collectors.toSet());
//
//        // Danh sách DataMovie name đã tồn tại trong DB
//        Set<String> existingNames = dataMovieRepository.findExistingName(inputMovieDataNames, ep.getId());
//
//
//        Episode episode = ep; // final hoặc effectively final
//        // Tạo các DataMovie mới
//        Set<DataMovie> newDataMovies = epResp.getDataMovies().stream().filter(dmResp -> !existingNames.contains(dmResp.getName())).map(dataMovieMapper::toEntity).peek(dm -> dm.setEpisode(episode)).collect(Collectors.toSet());
//
//        // Add vào tập hiện có (không clear())
//        ep.getDataMovies().addAll(newDataMovies);
//
//        return episodeRepository.save(ep);
//    }
    public Movie mapdataMovie(MovieResponse movieResponse) {
        Movie movie = movieMapper.toMovie(movieResponse);

        movie.setCreated(movieResponse.getCreated().getTime());

        movie.setModified(movieResponse.getModified().getTime());

        movie.setActors(mapData(
                movieResponse.getActors(),
                actorRepository::findByName,
                dataName -> actorRepository.save(Actor.builder().name(dataName).build()),
                actorRepository::save));

        movie.setDirectors(mapData(
                movieResponse.getDirectors(),
                directorRepository::findByName,
                dataName -> directorRepository.save(Director.builder().name(dataName).build()),
                directorRepository::save));

        movie.setCategories(mapData(
                movieResponse.getCategories(),
                data -> categoryRepository.findById(data.getId()),
                categoryMapper::toEntity,
                categoryRepository::save));

        movie.setCountries(mapData(
                movieResponse.getCountries(),
                data -> countryRepository.findById(data.getId()),
                countryMapper::toEntity,
                countryRepository::save));

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
        List<Episode> episodes = apiMovieResponse.getEpisodeResponseList().stream().map(epResp -> createEpisodeWithDataMovies(epResp, saveData)).toList();
        saveData.getEpisodes().addAll(episodes);
        return movieMapper.toMovieResponse(movieRepository.save(saveData));
    }

    private MovieResponse updateData(String apiUrl) {
        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);
        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
            throw new RuntimeException("API không trả dữ liệu movie");
        }
        MovieResponse movieResponse = apiMovieResponse.getMovie();
        Movie movie = movieRepository.findById(movieResponse.getId()).orElseThrow(() -> new RuntimeException("Movie chưa tồn tại trong DB"));
        List<Episode> updatedEpisodes = apiMovieResponse.getEpisodeResponseList().stream().map(epResp -> updateEpisodeWithDataMovies(epResp, movie)).toList();

        // movie.getEpisodes().clear();
        movie.getEpisodes().addAll(updatedEpisodes);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(savedMovie);
    }

    private Episode createEpisodeWithDataMovies(EpisodeResponse epResp, Movie movie) {
        Episode ep;
        ep = episodeMapper.toEntity(epResp);
        ep.setMovie(movie);
        ep = episodeRepository.save(ep);
        Episode episode = ep;
        Set<DataMovie> newDataMovies = epResp.getDataMovies().stream().map(dataMovieMapper::toEntity).peek(dm -> dm.setEpisode(episode)).collect(Collectors.toSet());
        ep.getDataMovies().addAll(newDataMovies);
        return episodeRepository.save(ep);
    }

    private Episode updateEpisodeWithDataMovies(EpisodeResponse epResp, Movie movie) {
        Optional<Episode> optionalEpisode = episodeRepository.findByMovieIdAndServerName(epResp.getServerName(), movie.getId());
        Episode episode;
        if (optionalEpisode.isPresent()) {
            episode = optionalEpisode.get();
            episode.setServerName(epResp.getServerName());
        } else {
            episode = episodeMapper.toEntity(epResp);
            episode.setMovie(movie);
            episode = episodeRepository.save(episode);
        }
        Episode ep = episode;
        Set<String> inputMovieDataNames = epResp.getDataMovies().stream().map(DataMovieResponse::getName).collect(Collectors.toSet());
        Set<String> existingNames = dataMovieRepository.findExistingName(inputMovieDataNames, ep.getId());
        Set<DataMovie> newDataMovies = epResp.getDataMovies().stream().filter(dmResp -> !existingNames.contains(dmResp.getName())).map(dataMovieMapper::toEntity).peek(dm -> dm.setEpisode(ep)).collect(Collectors.toSet());
        ep.getDataMovies().addAll(newDataMovies);
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
