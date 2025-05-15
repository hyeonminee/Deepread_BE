package com.deepread.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MypageStatisticsDto {
    private float avgSummaryScore;
    private float avgQuizAccuracy;
}
