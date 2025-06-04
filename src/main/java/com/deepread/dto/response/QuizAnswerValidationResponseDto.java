package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerValidationResponseDto {

    @Schema(description = "정답 여부", example = "true")
    private boolean isCorrect;

    @Schema(description = "정답 보기 번호 (1~4)", example = "2")
    private int correctOption;
}
