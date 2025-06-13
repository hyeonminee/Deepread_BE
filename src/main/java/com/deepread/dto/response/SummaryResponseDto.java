package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "요약 저장 응답 DTO")
public class SummaryResponseDto {

    @Schema(description = "요약 ID", example = "100")
    private Long id;

    @Schema(description = "AI 요약 결과")
    private String aiSummary;

    @Schema(description = "사용자 요약")
    private String userSummary;

    @Schema(description = "요약 피드백 점수 (0~100)")
    private Integer score;

    @Schema(description = "요약 피드백 내용")
    private String feedback;

    @Schema(hidden = true) // contentSnapshot은 숨김 처리
    private String contentSnapshot;
}
