//package com.example.ApiUser.controller;
//
//import com.example.ApiUser.dto.request.authentication.users.UserCreationRequest;
//import com.example.ApiUser.dto.request.authentication.users.UserUpdateRequest;
//import com.example.ApiUser.dto.response.authentication.UserResponse;
//import com.example.ApiUser.entity.authentication.users.User;
//import com.example.ApiUser.repository.authentication.UserRepository;
//import com.example.ApiUser.service.authentication.users.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Slf4j
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserService userService;
//    private UserCreationRequest userCreationRequest;
//    private UserUpdateRequest userUpdateRequest;
//    private UserResponse userResponse;
//    private User user;
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void initData(){
//        LocalDate localDate = LocalDate.of(2003, 2, 18);
//        userCreationRequest = UserCreationRequest.builder()
//                .username("nguyendacson")
//                .password("nguyendacson")
//                .email("dacsonn@gmail.com")
//                .dob(localDate)
//                .build();
//
//        userResponse = UserResponse.builder()
//                .id("nguyndac891934794y")
//                .username("nguyendacson")
//                .email("dacsonn@gmail.com")
//                .dob(localDate)
//                .build();
//    }
//
//    @Test
//    void createUser() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String content = objectMapper.writeValueAsString(userCreationRequest);
//
//        Mockito.when(userService.createUser(ArgumentMatchers.any()))
//                        .thenReturn(userResponse);
//
//        //WHEN
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/users")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(content)
//        )
//                //THEN
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
//    }
//
//    @Test
//    void createUser_invalid() throws Exception {
//        userCreationRequest.setUsername("nguyen");
//        when("1003","Username must be at least 8 characters");
//    }
//
//    @Test
//    void createUser_invalidPassword() throws Exception {
//        userCreationRequest.setPassword("1234");
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String content = objectMapper.writeValueAsString(userCreationRequest);
//
//        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .post("/users")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(content)
//        )
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1004))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password must be at least 8 characters"));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void getAllUser() throws Exception {
//        List<UserResponse> userResponseList = List.of();
//        Mockito.when(userService.getAllUser()).thenReturn(userResponseList);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/users")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("success"));
//    }
//
//    @Test
//    @WithMockUser(username = "nguyendacson", roles = {"USER"})
//    void getIdUser_success() throws Exception {
//        String userId = "12312323213";
//        userResponse.setId(userId);
//
//        Mockito.when(userService.getUser(userId)).thenReturn(userResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.
//                get("/users/{userId}", userId)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("success"));
//    }
//
//    @Test
//    @WithMockUser(username = "ngyendacson", roles = {"USER"})
//    void getMyInfo() throws Exception{
//        userResponse.setUsername("ngyendacson");
//        Mockito.when(userService.getMyInfo()).thenReturn(userResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/users/myInfo")
//                .contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    @WithMockUser(username = "nguyendacson", roles = {"USER"})
//    void putUser() throws Exception{
//        String userId = "nguyendacson1";
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//        String content = objectMapper.writeValueAsString(userUpdateRequest);
//        Mockito.when(userService.updateUser(userId,ArgumentMatchers.any()));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .put("users/{userId}", userId)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(content));
//    }
//
//
//    private void when(String code, String message) throws Exception {
//        // GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String content = objectMapper.writeValueAsString(userCreationRequest);
//
//        //WHEN
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/users")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(content)
//                )
//                //THEN
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 8 characters"));
//    }
//
//}
//
