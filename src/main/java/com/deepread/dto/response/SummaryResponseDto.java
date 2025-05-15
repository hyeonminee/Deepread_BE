package com.deepread.dto.response;

import com.deepread.dto.response.SummaryFeedbackResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryResponseDto {
    private Long summaryId;
    private String userSummary;
    private SummaryFeedbackResponseDto feedback;
}
