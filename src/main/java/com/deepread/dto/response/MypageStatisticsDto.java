package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MypageStatisticsDto {

    @Schema(description = "평균 요약 점수", example = "82.5")
    private float avgSummaryScore;

    @Schema(description = "평균 퀴즈 정답률", example = "76.0")
    private float avgQuizAccuracy;
}
