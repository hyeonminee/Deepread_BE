package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatbotLogRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @Schema(description = "사용자의 질문", example = "오늘 날씨 어때?")
    @NotBlank(message = "질문은 비어 있을 수 없습니다.")
    private String question;

    @Schema(description = "챗봇의 응답", example = "오늘은 맑고 기온은 23도입니다.")
    @NotBlank(message = "응답은 비어 있을 수 없습니다.")
    private String response;
}

