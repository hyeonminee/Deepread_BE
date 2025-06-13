package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "AI 요약 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiSummaryRequestDto {

    @Schema(description = "요약할 원문 텍스트", example = "고혈압은 유전적 요인과...")
    @NotBlank(message = "text는 비어 있을 수 없습니다.")
    private String text;
}
