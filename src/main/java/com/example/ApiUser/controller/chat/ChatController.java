package com.example.ApiUser.controller.chat;

import com.example.ApiUser.dto.request.chat.FilmInfo;
import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.service.chat.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat-image")
    ApiResponse<List<FilmInfo>> chatWithImage(@RequestParam(value = "file", required = false) MultipartFile file,
                                              @RequestParam("message") String message) {
        List<FilmInfo> list = chatService.chatWithImage(file, message);
        return ApiResponse.<List<FilmInfo>>builder()
                .result(list)
                .build();
    }
}
