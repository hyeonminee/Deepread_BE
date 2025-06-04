package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizStatisticsResponseDto {

    @Schema(example = "7")
    private Long userId;

    @Schema(example = "2025-06")
    private String month;

    @Schema(description = "풀이 횟수", example = "4")
    private Integer attemptCount;

    @Schema(description = "정답률 평균", example = "81.25")
    private Float averageAccuracy;
}
