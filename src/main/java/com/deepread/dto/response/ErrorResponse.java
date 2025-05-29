package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    @Schema(description = "에러 코드명", example = "BadRequest")
    private String error;

    @Schema(description = "에러 메시지", example = "요청 파라미터가 잘못되었습니다.")
    private String message;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private int status;

    @Schema(description = "요청 경로", example = "/api/summaries")
    private String path;

    @Schema(description = "에러 발생 시각", example = "2025-05-29T14:00:00")
    private LocalDateTime timestamp;
}
