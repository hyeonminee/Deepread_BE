package com.deepread.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizResultRequestDto {

    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @NotNull(message = "총 문항 수는 필수입니다.")
    @Min(value = 1, message = "총 문항 수는 1 이상이어야 합니다.")
    private Integer totalQuestions;

    @NotNull(message = "정답 개수는 필수입니다.")
    @Min(value = 0, message = "정답 개수는 0 이상이어야 합니다.")
    private Integer correctCount;

    @NotNull(message = "정답률은 필수입니다.")
    private Float accuracy;
}
