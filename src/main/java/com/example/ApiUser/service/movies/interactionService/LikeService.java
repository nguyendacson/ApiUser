package com.example.ApiUser.service.movies.interactionService;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.Likes;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.repository.movies.interationMovie.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeService {
    MovieCallRepository movieCallRepository;
    LikeRepository likeRepository;
    UserRepository userRepository;
    MovieDTOMapper movieDTOMapper;

    @Transactional
    public void createLike(String movieId, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        if (likeRepository.existsByUserAndMovie(user, movie)) {
            throw new AppException(ErrorCode.MOV_USER_EXISTED);
        }

        Likes likes = Likes.builder()
                .movie(movie)
                .user(user)
                .build();

        likeRepository.save(likes);
    }

    public void deleteLike(String movieId, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieCallRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        Likes likes = likeRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_LIKE_NOT_FOUND));

        likeRepository.delete(likes);
    }

    public List<MovieDTO> getAllLikeByUser(String userId, Sort sort) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        List<Likes> likes = likeRepository.findAllByUser(user, sort);
        if (likes.isEmpty()) {
            throw new AppException(ErrorCode.MOV_LIKE_NOT_FOUND);
        }

        return likes.stream()
                .map(Likes::getMovie)
                .map(movieDTOMapper::toDTO)
                .toList();
    }
}
