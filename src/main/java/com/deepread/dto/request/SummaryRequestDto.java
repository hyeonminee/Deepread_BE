package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "콘텐츠 ID", example = "1001")
    @NotNull
    private Long contentId;

    @Schema(description = "사용자가 입력한 요약문", example = "이 글은 건강한 식습관의 중요성을 설명한다.")
    @NotBlank(message = "요약문을 입력해주세요.")
    private String userSummary;
}
