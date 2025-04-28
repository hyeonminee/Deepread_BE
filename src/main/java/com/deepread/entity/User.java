package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    @Column(unique = true, nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private Level user_level = Level.초급;

    private String imageUrl;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum SocialProvider {
        kakao, naver, google
    }

    public enum Level {
        초급, 중급, 고급
    }
}
