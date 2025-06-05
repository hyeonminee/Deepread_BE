package com.deepread.dto.response;

import com.deepread.entity.Summary;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummarySimpleDto {
    private Long summaryId;
    private String title;
    private String userSummary;
    private String aiSummary;

    public static SummarySimpleDto from(Summary summary) {
        return new SummarySimpleDto(
                summary.getId(),
                summary.getContent().getTitle(),  // Content 엔티티의 title 필드 기준
                summary.getUserSummary(),
                summary.getAiSummary()
        );
    }
}
