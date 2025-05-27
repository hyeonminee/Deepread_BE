package com.deepread.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiagnosisEvaluationRequestDto {
    private List<AnswerSubmission> answers;

    @Getter
    @Setter
    public static class AnswerSubmission {
        private Long id;           // 문제 ID
        private Integer answer;    // 사용자가 선택한 답 (1~4)
    }
}
