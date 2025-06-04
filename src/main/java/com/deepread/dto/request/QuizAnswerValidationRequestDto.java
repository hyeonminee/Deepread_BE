package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizAnswerValidationRequestDto {

    @Schema(description = "문제 ID", example = "101")
    @NotNull(message = "questionId는 필수입니다.")
    private Long questionId;

    @Schema(description = "선택한 보기 번호 (1~4)", example = "3")
    @NotNull(message = "선택한 답안은 필수입니다.")
    private Integer selectedOption;
}
