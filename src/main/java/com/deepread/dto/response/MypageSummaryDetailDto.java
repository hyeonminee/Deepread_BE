package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "마이페이지 상세 요약 응답 DTO")
public class MypageSummaryDetailDto {

    @Schema(description = "사용자가 작성한 요약", example = "이 글은 세금 신고 절차에 대한 설명입니다.")
    private String userSummary;

    @Schema(description = "AI가 작성한 요약", example = "세금 신고는 일정에 따라 단계적으로 이루어진다.")
    private String aiSummary;

    @Schema(description = "AI가 평가한 점수", example = "88.9")
    private Double score;

    @Schema(description = "AI 피드백 문장", example = "핵심 내용을 잘 요약하였습니다...")
    private String feedback;

    @Schema(description = "요약 당시 저장된 원문", example = "세금 신고는 매년 5월 말까지 ...")
    private String contentSnapshot;
}
