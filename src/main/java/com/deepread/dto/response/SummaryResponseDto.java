package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryResponseDto {

    @Schema(description = "요약 ID", example = "10")
    private Long id;

    @Schema(description = "사용자 요약", example = "이 글은 세금 신고 절차에 대한 설명입니다.")
    private String userSummary;

    @Schema(description = "AI 요약", example = "세금 신고는 일정에 따라 단계적으로 이루어진다.")
    private String aiSummary;

    @Schema(description = "AI 채점 점수", example = "88.9")
    private Double score;

    @Schema(description = "AI 피드백", example = "요약이 잘 반영됨")
    private String feedback;

    @Schema(description = "요약 당시 원문 복사", example = "세금 신고는 매년 ...")
    private String contentSnapshot;
}
