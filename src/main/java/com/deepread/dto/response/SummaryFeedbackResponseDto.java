package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryFeedbackResponseDto {

    @Schema(description = "요약 점수", example = "88.2")
    private Float score;

    @Schema(description = "AI 피드백 문장", example = "핵심 내용을 잘 요약하였습니다.")
    private String feedbackText;
}
