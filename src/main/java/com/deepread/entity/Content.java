package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content; // 원문 텍스트

    @Lob
    private String aiSummary; // AI 기준 요약문

    @Enumerated(EnumType.STRING)
    private User.Level level;

    private LocalDateTime createdAt = LocalDateTime.now();
}