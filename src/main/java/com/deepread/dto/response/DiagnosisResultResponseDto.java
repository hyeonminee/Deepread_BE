package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.deepread.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiagnosisResultResponseDto {

    @Schema(description = "진단 결과 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 ID", example = "5")
    private Long userId;

    @Schema(description = "진단 점수", example = "75")
    private Integer score;

    @Schema(description = "사용자의 문해력 수준", example = "고급")
    private User.Level userLevel;

    @Schema(description = "진단 완료 일시", example = "2025-05-29T13:20:00")
    private LocalDateTime createdAt;
}
