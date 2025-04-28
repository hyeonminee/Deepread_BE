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

    private Integer score;

    @Lob
    private String comments; // JSON 문자열로 저장

    private LocalDateTime generatedAt = LocalDateTime.now();
}
