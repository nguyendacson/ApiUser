package com.example.ApiUser.service.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(MultipartFile file, String message) {

        if ((file == null || file.isEmpty()) && (message == null || message.isBlank())) {
            return "Không có dữ liệu để xử lý.";
        }

        if (file != null && !file.isEmpty()) {

            String systemPrompt = """
                        Nếu hình ảnh liên quan đến phim, hãy mô tả tên phim, năm phát hành và nội dung chính bằng tiếng Việt.
                        Không dựa vào chữ trên ảnh.
                        Nếu không nhận diện được phim, hãy trả lời: 'Không xác định được phim từ hình ảnh này.'
                    """;

            Media media = Media.builder()
                    .mimeType(MimeTypeUtils.parseMimeType(
                            Objects.requireNonNull(file.getContentType())))
                    .data(file.getResource())
                    .build();

            String result = chatClient.prompt()
                    .system(systemPrompt)
                    .user(spec -> spec.media(media))
                    .call()
                    .entity(String.class);

            return (result == null || result.isBlank())
                    ? "Không xác định được phim từ hình ảnh này."
                    : result;
        }


        String systemPrompt = """
                    Bạn là trợ lý AI cho ứng dụng Movie SeeMe do Nguyễn Đắc Sơn sáng lập.
                    Bạn có thể trả lời mọi câu hỏi bằng tiếng Việt.
                    Nếu câu hỏi liên quan đến phim, hãy trả lời thông tin phim bằng tiếng Việt.
                    Nếu câu hỏi tổng quát (như 'Bạn là ai?'), hãy trả lời một cách tự nhiên bằng tiếng Việt.
                    Hướng dẫn người dùng các tình huống thường gặp:
                    1. Chỉnh màn hình sáng/tối: Vào Cài đặt → Cài đặt chung → Giao diện → Chọn kiểu muốn bạn muốn thay đổi.
                    2. Thay đổi thông tin tài khoản: Vào Cài đặt → Tài khoản của bạn → Chọn thông tin cần thay đổi.
                    3. Đăng ký email: Đăng ký email để phòng khi quên mật khẩu có thể khôi phục.
                    4. Xác thực email: Cần xác thực để email có thể dùng khôi phục mật khẩu.
                    5. Khó khăn/thắc mắc liên quan sản phẩm: Liên hệ Nguyễn Đắc Sơn, email: dacson1822003@gmail.com
                    6. Tại sao không download được: Tính năng này đang phát triển.
                    7. Muốn thêm email, thay đổi email làm th nào: Vào cài đặt, chọn tài khoản của bạn, chọn email sau đó nhập email muốn liên kết, sau đó ấn đăng kí/ cập nhật. Nếu thông báo thành công vào gmail để xác thưc email
                    Nếu câu hỏi liên quan đến phim, trả lời thông tin phim bằng tiếng Việt.
                    Nếu là câu hỏi chung, trả lời tự nhiên, rõ ràng bằng tiếng Việt.
                """;
        return chatClient.prompt()
                .system(systemPrompt)
                .user(spec -> spec.text(message))
                .call()
                .entity(String.class);
    }
}
