package com.example.ApiUser.controller.chat;

import com.example.ApiUser.dto.response.authentication.ApiResponse;
import com.example.ApiUser.service.chat.ChatService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ApiResponse<String> chat(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "message", required = false) String message
    ) {
        String result = chatService.chat(file, message);

        return ApiResponse.<String>builder()
                .result(result)
                .build();
    }
}
