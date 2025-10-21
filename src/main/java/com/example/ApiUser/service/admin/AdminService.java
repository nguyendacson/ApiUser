package com.example.ApiUser.service.admin;

import com.example.ApiUser.dto.response.admin.CountMovie;
import com.example.ApiUser.dto.response.admin.UserResponseAdmin;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.authentication.UserMapper;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.movies.interationMovie.LikeRepository;
import com.example.ApiUser.repository.movies.interationMovie.WatchingRepository;
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
    LikeRepository likeRepository;
    WatchingRepository watchingRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseAdmin> getAllUser() {
        List<User> users = userRepository.findAll();
        return userMapper.toListUserResponseAdmin(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseAdmin userInfor(String key) {
        User user = userRepository.findByEmail(key)
                .or(() -> userRepository.findByUsername(key))
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        return userMapper.toUserResponseAdmin(user);
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
