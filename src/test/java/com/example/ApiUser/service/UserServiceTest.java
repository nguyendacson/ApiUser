package com.example.ApiUser.service;

import com.example.ApiUser.dto.request.authentication.users.UserCreationRequest;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.service.authentication.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;
    private UserCreationRequest userCreationRequest;
    private User user;

    @BeforeEach
    void initData(){
        LocalDate localDate = LocalDate.of(2003, 2, 18);
        userCreationRequest = UserCreationRequest.builder()
                .username("nguyendacson")
                .password("nguyendacson")
                .email("dacsonn@gmail.com")
                .dob(localDate)
                .build();

        user = User.builder()
                .id("nguyndac891934794y")
                .username("nguyendacson")
                .email("dacsonn@gmail.com")
                .dob(localDate)
                .build();
    }

//    @Test
//    void createUser() {
//        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
//        Mockito.when(userRepository.save(any())).thenReturn(user);
//
//        var response = userService.createUser(userCreationRequest);
//
//        Assertions.assertThat(response.getId()).isEqualTo("nguyndac891934794y");
//    }

    @Test
    void createUser_fail() {
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // when
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(userCreationRequest));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }
}
