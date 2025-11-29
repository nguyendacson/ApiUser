package com.example.ApiUser.service.movies.interactionService;

import com.example.ApiUser.dto.request.movies.watching.UpdateWatchingRequest;
import com.example.ApiUser.dto.request.movies.watching.WatchingRequest;
import com.example.ApiUser.dto.response.movies.WatchingResponse;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.DataMovie;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Watching;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.callMovies.DataMovieRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.repository.movies.interationMovie.WatchingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WatchingService {
    UserRepository userRepository;
    MovieCallRepository movieRepository;
    MovieDTOMapper movieDTOMapper;
    DataMovieRepository dataMovieRepository;
    WatchingRepository watchingRepository;

    public List<WatchingResponse> allWatchingByUser(String userId, String filter) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        List<Watching> listWatching = watchingRepository.findAllByUser(user);
        if (listWatching.isEmpty()) {
            throw new AppException(ErrorCode.MOV_NOT_FOUND);
        }

        return listWatching.stream()
                .filter(w -> filter == null || filter.isEmpty() || filter.equals(w.getMovie().getType()))
                .map(item -> WatchingResponse.builder()
                        .movieDTO(movieDTOMapper.toDTO(item.getMovie()))
                        .id(item.getId())
                        .dataMovieId(item.getDataMovie().getId())
                        .dataMovieName(item.getDataMovie().getFilename())
                        .progressSeconds(item.getProgressSeconds())
                        .lastWatchedAt(item.getLastWatchedAt())
                        .build())
                .toList();
    }

    public void createWatching(WatchingRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        DataMovie dataMovie = dataMovieRepository.findById(request.getDataMovieId())
                .orElseThrow(() -> new AppException(ErrorCode.MOV_DATA_NOT_FOUND));

        if (watchingRepository.existsByUserAndMovieAndDataMovie(user, movie, dataMovie)) {
            throw new AppException(ErrorCode.MOV_USER_DATA_EXISTED);
        }

        Watching watching = Watching.builder()
                .user(user)
                .movie(movie)
                .dataMovie(dataMovie)
                .progressSeconds(request.getProgressSeconds())
                .lastWatchedAt(
                        request.getLastWatchedAt() != null
                                ? request.getLastWatchedAt()
                                : LocalDateTime.now()
                )
                .build();

        watchingRepository.save(watching);
    }

    public void deleteWatching(String dataMovieId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        DataMovie dataMovie = dataMovieRepository.findById(dataMovieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_DATA_NOT_FOUND));


        Watching watching = watchingRepository.findByUserAndDataMovie(user, dataMovie)
                .orElseThrow(() -> new AppException(ErrorCode.LST_NOT_FOUND));

        watchingRepository.delete(watching);
    }


    public void updateWatching(UpdateWatchingRequest updateWatchingRequest, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        DataMovie dataMovie = dataMovieRepository.findById(updateWatchingRequest.getDataMovieId())
                .orElseThrow(() -> new AppException(ErrorCode.MOV_DATA_NOT_FOUND));


        Watching watching = watchingRepository.findByUserAndDataMovie(user, dataMovie)
                .orElseThrow(() -> new AppException(ErrorCode.LST_NOT_FOUND));

        watching.setProgressSeconds(updateWatchingRequest.getNewProgressSeconds());
        watchingRepository.save(watching);

    }


    @Transactional
    public void deleteOldWatchingList() {
//        LocalDateTime threshold = LocalDateTime.now().minusMonths(6);
        LocalDateTime threshold = LocalDateTime.now().minusMonths(6);
        int deletedCount = watchingRepository.deleteByLastWatchedAtBefore(threshold);
        log.info("Đã xóa {} bản ghi Watching cũ hơn {}", deletedCount, threshold);
    }
}