package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "챗봇 로그 저장 요청 DTO")
public class ChatbotLogRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @Schema(description = "질문한 단어", example = "사과")
    @NotBlank(message = "단어는 비어 있을 수 없습니다.")
    private String word;

    @Schema(description = "챗봇 응답", example = "사과는 과일로, 빨갛고 달콤한 맛이 있습니다.")
    @NotBlank(message = "응답은 비어 있을 수 없습니다.")
    private String response;
}
