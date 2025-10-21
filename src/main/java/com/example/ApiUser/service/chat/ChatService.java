package com.example.ApiUser.service.chat;

import com.example.ApiUser.dto.request.chat.FilmInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public List<FilmInfo> chatWithImage(MultipartFile file, String message) {
        String systemPrompt = """
    You are an AI assistant for the Movie SeeMe app.
    You will support users with questions about movies from the Movie SeeMe database.
    When receiving an image, identify if it corresponds to a movie.
    Always respond with a JSON array of FilmInfo objects.
    If the image does not contain a movie, respond with an empty array [].
    If the image contains a movie, do NOT rely on the text in the image. Instead, search online or use public movie databases to get accurate information.
    If the user asks for support, respond with [{"title":"Contact Admin","info":"contact admin dacson1822003@gmail.com"}].
""";


        var promptBuilder = chatClient.prompt()
                .system(systemPrompt);

        if (file == null || file.isEmpty()) {
            return promptBuilder
                    .user(spec -> spec.text(message))
                    .call()
                    .entity(new ParameterizedTypeReference<List<FilmInfo>>() {
                    });
        } else {
            Media media = Media.builder()
                    .mimeType(MimeTypeUtils.parseMimeType(
                            Objects.requireNonNull(file.getContentType())))
                    .data(file.getResource())
                    .build();

            return promptBuilder
                    .user(spec -> spec
                            .media(media)
                            .text(message))
                    .call()
                    .entity(new ParameterizedTypeReference<List<FilmInfo>>() {
                    });
        }
    }
}
