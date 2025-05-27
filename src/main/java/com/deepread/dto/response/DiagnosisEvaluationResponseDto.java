package com.deepread.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiagnosisEvaluationResponseDto {
    private Integer score;         // 총점 (100점 만점)
    private List<Boolean> results; // 각 문제 정답 여부
}
