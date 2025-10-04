package com.example.ApiUser.service.movies;

import com.example.ApiUser.dto.request.movies.WatchingCreateRequest;
import com.example.ApiUser.dto.response.movies.WatchingResponse;
import com.example.ApiUser.entity.movies.DataMovie;
import com.example.ApiUser.entity.movies.Movie;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.entity.relationships.WatchingList;
import com.example.ApiUser.entity.user.User;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.respository.UserRepository;
import com.example.ApiUser.respository.callData.DataMovieRepository;
import com.example.ApiUser.respository.callData.MovieRepository;
import com.example.ApiUser.respository.movies.WatchingListRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WatchingListService {
    UserRepository userRepository;
    MovieRepository movieRepository;
    MovieDTOMapper movieDTOMapper;
    DataMovieRepository dataMovieRepository;
    WatchingListRepository watchingListRepository;

    public List<WatchingResponse> getListWatching(String userId) {
        List<WatchingList> watchingLists = watchingListRepository.findByUserId(userId);

        if (!watchingListRepository.existsByUser_Id(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        List<WatchingResponse> responses = watchingLists.stream()
                .map(item -> WatchingResponse.builder()
                        .movieDTO(MovieDTO.builder()
                                .id(item.getMovie().getId())
                                .name(item.getMovie().getName())
                                .slug(item.getMovie().getSlug())
                                .origin_name(item.getMovie().getOrigin_name())
                                .poster_url(item.getMovie().getPoster_url())
                                .thumb_url(item.getMovie().getThumb_url())
                                .episode_current(item.getMovie().getEpisode_current())
                                .episode_total(item.getMovie().getEpisode_total())
                                .quality(item.getMovie().getQuality()).build())
                        .progressSeconds(item.getProgressSeconds())
                        .lastWatchedAt(item.getLastWatchedAt())
                        .build())
                .toList();
        log.info("responses = {}", responses);

        return responses;
    }

    public WatchingResponse createWatchingList(WatchingCreateRequest watchingCreateRequest) {
        String getIdUser = watchingCreateRequest.getUserId();
        String getIdMovie = watchingCreateRequest.getMovieId();
        String getIdDataMovie = watchingCreateRequest.getDataMovieId();

        if (watchingListRepository.existsByUserAndMovieAndDataMovie(getIdUser, getIdMovie, getIdDataMovie)) {
            throw new AppException(ErrorCode.USER_DATA_MOVIE_NOT_EXISTED);
        }

        User user = userRepository.findById(getIdUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Movie movie = movieRepository.findById(getIdMovie)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));

        DataMovie dataMovie = dataMovieRepository.findById(getIdDataMovie)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_MOVIE_NOT_EXISTED));

        WatchingList watchingList = WatchingList.builder()
                .user(user)
                .movie(movie)
                .dataMovie(dataMovie)
                .progressSeconds(watchingCreateRequest.getProgressSeconds())
                .lastWatchedAt(
                        watchingCreateRequest.getLastWatchedAt() != null
                                ? watchingCreateRequest.getLastWatchedAt()
                                : LocalDateTime.now()
                )
                .build();

        WatchingList saved = watchingListRepository.save(watchingList);

        return WatchingResponse.builder()
                .progressSeconds(saved.getProgressSeconds())
                .lastWatchedAt(saved.getLastWatchedAt())
                .movieDTO(movieDTOMapper.toDTO(saved.getMovie()))
                .build();
    }

}
