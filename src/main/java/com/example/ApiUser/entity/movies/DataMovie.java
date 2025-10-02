package com.example.ApiUser.entity.movies;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

//    @Column(unique = true)
    String name;
    String slug;
    String filename;
    String link_embed;
    String link_m3u8;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    Episode episode;


}
