package com.deepread.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diagnosis_questions")
@IdClass(DiagnosisQuestionId.class)
@Getter
@Setter
@NoArgsConstructor
public class DiagnosisQuestion {

    @Id
    private String type;  // 복합키 구성

    @Id
    private Long id;  // 복합키 구성

    @Column(columnDefinition = "TEXT")
    private String passage;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String option1;

    @Column(columnDefinition = "TEXT")
    private String option2;

    @Column(columnDefinition = "TEXT")
    private String option3;

    @Column(columnDefinition = "TEXT")
    private String option4;

    private Integer answer;

    @PrePersist
    public void validateId() {
        if (id == null || id < 1 || id > 5) {
            throw new IllegalArgumentException("각 type별 id는 1~5 사이여야 합니다.");
        }
    }
}