package com.deepread.dto.response;

import com.deepread.entity.Summary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "간단한 요약 정보 응답 DTO")
public class SummarySimpleDto {

    @Schema(description = "요약 ID", example = "11")
    private Long summaryId;

    @Schema(description = "콘텐츠 제목", example = "고혈압")
    private String title;

    @Schema(description = "사용자가 작성한 요약", example = "고혈압은 유전과 환경 요인이 복합적으로 작용하여 발생합니다.")
    private String userSummary;

    @Schema(description = "AI가 생성한 요약", example = "고혈압은 생활습관과 관련이 있으며 초기 증상이 없습니다.")
    private String aiSummary;

    public static SummarySimpleDto from(Summary summary) {
        return SummarySimpleDto.builder()
                .summaryId(summary.getId())
                .title(summary.getContent().getTitle())
                .userSummary(summary.getUserSummary())
                .aiSummary(summary.getAiSummary())
                .build();
    }
}
