package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "퀴즈 결과 저장 요청 DTO")
public class QuizResultRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @Schema(description = "총 문항 수", example = "5")
    @NotNull(message = "총 문항 수는 필수입니다.")
    @Min(value = 1, message = "총 문항 수는 1 이상이어야 합니다.")
    private Integer totalQuestions;

    @Schema(description = "정답 개수", example = "5")
    @NotNull(message = "정답 개수는 필수입니다.")
    @Min(value = 0, message = "정답 개수는 0 이상이어야 합니다.")
    private Integer correctCount;

    @Schema(description = "정답률")
    @NotNull(message = "정답률은 필수입니다.")
    private Float accuracy;
}