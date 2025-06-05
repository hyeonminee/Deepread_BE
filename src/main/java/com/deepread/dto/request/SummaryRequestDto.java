package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "콘텐츠 ID (Content 테이블의 ID)", example = "10")
    private Long contentId;

    @Schema(description = "사용자가 작성한 요약", example = "이 법안은 ...")
    private String userSummary;

    @Schema(description = "ai가 작성한 요약")
    private String aiSummary;
}
