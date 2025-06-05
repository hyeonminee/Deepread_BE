package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatbotLogResponseDto {

    @Schema(description = "챗봇 로그 ID")
    private Long id;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "질문한 단어")
    private String word;

    @Schema(description = "챗봇의 응답")
    private String response;

    @Schema(description = "생성 일시", example = "2025-05-29T15:00:00")
    private LocalDateTime createdAt;
}
