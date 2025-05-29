package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class SummaryResponseDto {

    @Schema(description = "요약 ID", example = "10")
    private Long summaryId;

    @Schema(description = "사용자가 작성한 요약문", example = "이 글은 환경 보호의 중요성을 강조하고 있다.")
    private String userSummary;

    @Schema(description = "AI의 요약 피드백 결과")
    private SummaryFeedbackResponseDto feedback;

    @Schema(description = "추론 결과, BERT 점수 등 상세 평가 결과")
    private Map<String, Object> evaluationResult;
}
