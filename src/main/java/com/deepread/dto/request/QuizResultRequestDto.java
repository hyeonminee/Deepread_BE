package com.deepread.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizResultRequestDto {
    @NotNull
    private Long userId;

    private Integer correctCount;
    private Integer totalQuestions;
    private Float accuracy;
}
