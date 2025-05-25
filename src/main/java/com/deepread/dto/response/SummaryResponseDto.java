package com.deepread.dto.response;

import com.deepread.dto.response.SummaryFeedbackResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SummaryResponseDto {
    private Long summaryId; // 요약 ID
    private String userSummary; // 사용자가 작성한 요약문
    private SummaryFeedbackResponseDto feedback; // 점수 + 피드백 문장 DTO
    private Map<String, Object> evaluationResult; // 로우 데이터 (entailment, BERT, 기타)

}
