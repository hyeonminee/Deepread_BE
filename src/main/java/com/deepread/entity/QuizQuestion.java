package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;    // 문제 본문

    private String option1;     // 보기 1
    private String option2;     // 보기 2
    private String option3;     // 보기 3
    private String option4;     // 보기 4

    private Integer answer;     // 정답 (1~4 중 정답 위치)

    @Enumerated(EnumType.STRING)
    private QuizLevel level; // 문해력 난이도
}
