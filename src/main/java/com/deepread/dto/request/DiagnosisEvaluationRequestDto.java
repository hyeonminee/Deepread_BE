package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "진단 평가 요청 DTO")
public class DiagnosisEvaluationRequestDto {

    @NotNull(message = "답안 목록은 필수입니다.")
    @Schema(description = "사용자의 각 답안 목록")
    private List<@Valid AnswerSubmission> answers; // 내부 유효성 검사

    @Getter
    @Setter
    public static class AnswerSubmission {

        @NotNull(message = "문제 ID는 필수입니다.")
        @Schema(description = "문제 ID", example = "5")
        private Long id;

        @NotNull(message = "답변은 필수입니다.")
        @Schema(description = "사용자가 선택한 답변 (1~4)", example = "3")
        private Integer answer;
    }
}
