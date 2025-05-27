package com.deepread.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diagnosis_questions")
@Getter
@Setter
@NoArgsConstructor
public class DiagnosisQuestion {

    @Id
    private Long id; // type 내에서 1~5 범위의 고정 id

    private String type; // 'A', 'B', 'C'

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

    // 복합 유니크 제약 (type + id)
    @PrePersist
    public void validateId() {
        if (id == null || id < 1 || id > 5) {
            throw new IllegalArgumentException("각 type별 id는 1~5 사이여야 합니다.");
        }
    }
}
