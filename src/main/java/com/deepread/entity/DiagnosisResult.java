package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnosis_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 연관관계 (N:1)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer score;

    @Enumerated(EnumType.STRING)
    private User.Level userLevel;

    private LocalDateTime createdAt = LocalDateTime.now();
}
