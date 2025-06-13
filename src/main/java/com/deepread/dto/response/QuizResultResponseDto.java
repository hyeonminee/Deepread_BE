package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuizResultResponseDto {

    @Schema(description = "퀴즈 결과 ID", example = "3")
    private Long id;

    @Schema(description = "사용자 ID", example = "7")
    private Long userId;

    @Schema(description = "정답 개수", example = "4")
    private Integer correctCount;

    @Schema(description = "총 문항 수", example = "5")
    private Integer totalQuestions;

    @Schema(description = "정답률", example = "80.0")
    private Float accuracy;

    @Schema(description = "제출 시각", example = "2025-05-29T12:45:00")
    private LocalDateTime submittedAt;
}

