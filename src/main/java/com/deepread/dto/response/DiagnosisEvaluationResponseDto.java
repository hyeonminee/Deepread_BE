package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiagnosisEvaluationResponseDto {

    @Schema(description = "총점 (100점 만점)", example = "85")
    private Integer score;

    @Schema(description = "각 문제 정답 여부 리스트", example = "[true, false, true]")
    private List<Boolean> results;
}
