package com.example.ApiUser.dto.request.waiting;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataMovieRequest {
    String name;
    String slug;
    String filename;
    String link_embed;
    String link_m3u8;
}
