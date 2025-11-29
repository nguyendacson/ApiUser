package com.example.ApiUser.service.admin;

import com.example.ApiUser.dto.response.admin.CountMovie;
import com.example.ApiUser.dto.response.admin.UserResponseAdmin;
import com.example.ApiUser.dto.response.authentication.UserResponse;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.movies.*;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.authentication.UserMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.interationMovie.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {
    private final TrailerRepository trailerRepository;
    private final ListForYouRepository listForYouRepository;
    private final CommentRepository commentRepository;
    MyListRepository myListRepository;
    LikeRepository likeRepository;
    WatchingRepository watchingRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return userMapper.toListUserResponse(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseAdmin userInfor(String key) {
        User user = userRepository.findByEmail(key)
                .or(() -> userRepository.findByUsername(key))
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        return userMapper.toUserResponseAdmin(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String key) {
        User user = userRepository.findByEmail(key)
                .or(() -> userRepository.findByUsername(key))
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        List<Watching> watching = watchingRepository.findAllByUser(user);
        watchingRepository.deleteAll(watching);

        List<Likes> likes = likeRepository.findAllByUser(user);
        likeRepository.deleteAll(likes);

        List<MyList> myLists = myListRepository.findAllByUser(user);
        myListRepository.deleteAll(myLists);

        List<Comment> comments = commentRepository.findAllByUser(user);
        commentRepository.deleteAll(comments);

        List<ListForYou> listForYous = listForYouRepository.findAllByUser(user);
        listForYouRepository.deleteAll(listForYous);

        List<Trailer> trailerList = trailerRepository.findAllByUser(user);
        trailerRepository.deleteAll(trailerList);

        userRepository.delete(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CountMovie> getAllMovieWatching() {
        return watchingRepository.findMovieWatchCounts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CountMovie> getAllMovieLike() {
        return likeRepository.findMovieListCounts();
    }
}
