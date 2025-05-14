package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "summary_feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관된 요약
    @OneToOne
    @JoinColumn(name = "summary_id", nullable = false, unique = true)
    private Summary summary;

    // AI가 평가한 점수
    private Float score;

    // AI가 생성한 피드백 텍스트
    @Column(length = 2000)
    private String feedbackText;

    // 피드백 생성 시작
    private LocalDateTime generatedAt = LocalDateTime.now();
}
