package com.example.ApiUser.service.movies.interactionService;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.entity.movies.Trailer;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.repository.movies.interationMovie.TrailerRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrailerService {
    MovieCallRepository movieCallRepository;
    TrailerRepository trailerRepository;
    UserRepository userRepository;
    private final MovieDTOMapper movieDTOMapper;

    @Transactional
    public void createTrailer(String movieId, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        if (trailerRepository.existsByUserAndMovie(user, movie)) {
            throw new AppException(ErrorCode.MOV_USER_EXISTED);
        }

        Trailer trailers = Trailer.builder()
                .movie(movie)
                .user(user)
                .build();

        trailerRepository.save(trailers);
    }

    public void deleteTrailer(String movieId, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        Trailer trailer = trailerRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_LIKE_NOT_FOUND));

        trailerRepository.delete(trailer);
    }

    public List<MovieDTO> getAllTrailerUser(String userId, Sort sort, String filter) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        List<Trailer> trailers = trailerRepository.findAllByUser(user, sort);

        if (trailers.isEmpty()) {
            throw new AppException(ErrorCode.MOV_NOT_FOUND);
        }

        return trailers.stream()
                .map(Trailer::getMovie)
                .filter(movie -> filter == null || filter.isEmpty() || filter.equals(movie.getType()))
                .map(movieDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
