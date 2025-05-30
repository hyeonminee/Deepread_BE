package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer correctCount;

    private Integer totalQuestions;

    private Float accuracy;

    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
        if (accuracy != null && (accuracy < 0.0f || accuracy > 100.0f)) {
            throw new IllegalArgumentException("정확도(accuracy)는 0~100 사이여야 합니다.");
        }
    }
}