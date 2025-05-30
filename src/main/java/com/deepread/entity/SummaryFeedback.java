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

    @OneToOne
    @JoinColumn(name = "summary_id", nullable = false, unique = true)
    private Summary summary;

    @Column(nullable = false)
    private Float score;

    @Column(nullable = false, length = 2000)
    private String feedbackText;

    private LocalDateTime generatedAt;

    @PrePersist
    public void prePersist() {
        if (generatedAt == null) {
            generatedAt = LocalDateTime.now();
        }
    }
}
