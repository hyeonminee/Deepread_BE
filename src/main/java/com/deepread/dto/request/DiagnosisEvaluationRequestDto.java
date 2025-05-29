package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiagnosisEvaluationRequestDto {

    @Schema(description = "사용자의 각 답안 목록")
    private List<AnswerSubmission> answers;

    @Getter
    @Setter
    public static class AnswerSubmission {
        @Schema(description = "문제 ID", example = "101")
        private Long id;

        @Schema(description = "사용자가 선택한 답변 (1~4)", example = "3")
        private Integer answer;
    }
}
