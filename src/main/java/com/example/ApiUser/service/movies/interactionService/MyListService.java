package com.example.ApiUser.service.movies.interactionService;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.MovieDTO;
import com.example.ApiUser.entity.movies.MyList;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.movies.MovieDTOMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.MovieCallRepository;
import com.example.ApiUser.repository.movies.interationMovie.MyListRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MyListService {
    private final MovieDTOMapper movieDTOMapper;
    UserRepository userRepository;
    MovieCallRepository movieRepository;
    MyListRepository myListRepository;

    public void createMyList(String movieId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        if (myListRepository.existsByUserAndMovie(user, movie)) {
            throw new AppException(ErrorCode.LST_EXISTED);
        }

        MyList myList = MyList.builder()
                .movie(movie)
                .user(user)
                .build();
        myListRepository.save(myList);
    }

    public void deleteMyList(String movieId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOV_NOT_FOUND));

        MyList myList = myListRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new AppException(ErrorCode.LST_NOT_FOUND));

        myListRepository.delete(myList);
    }

    public List<MovieDTO> allMyLists(String userId, Sort sort) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        List<MyList> myLists = myListRepository.findAllByUser(user, sort);
        if (myLists.isEmpty()) {
            throw new AppException(ErrorCode.MOV_NOT_FOUND);
        }

        return myLists.stream()
                .map(MyList::getMovie)
                .map(movieDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}