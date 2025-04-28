package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String reportMonth; // 'YYYY-MM'

    private Integer summaryAverage;

    private Float quizAccuracy;

    private Integer activeDays;

    @Enumerated(EnumType.STRING)
    private User.Level user_level;
}
