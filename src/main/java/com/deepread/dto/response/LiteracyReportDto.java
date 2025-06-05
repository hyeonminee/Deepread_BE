package com.deepread.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiteracyReportDto {
    private LocalDate startDate;     // 평가 시작일
    private LocalDate endDate;       // 평가 종료일
    private double summaryAvg;       // 요약 점수 평균
    private double quizAvg;          // 퀴즈 점수 평균
    private double totalAvg;         // 총 평균
}