package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "요약 저장 요청 DTO")
public class SummaryRequestDto {

    @Schema(description = "사용자 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "콘텐츠 ID", example = "21", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contentId;

    @Schema(description = "사용자 요약", example = "고혈압은 유전적 요인과 환경적 요인이 작용하여 발생하며, 두통과 같은 증상이 나타날 수 있다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userSummary;
}
