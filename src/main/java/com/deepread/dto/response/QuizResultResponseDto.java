package com.deepread.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuizResultResponseDto {
    private Long id;
    private Long userId;
    private Integer correctCount;
    private Integer totalQuestions;
    private Float accuracy;
    private LocalDateTime submittedAt;
}
