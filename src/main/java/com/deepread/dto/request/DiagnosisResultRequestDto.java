package com.deepread.dto.request;

import com.deepread.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisResultRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @Schema(description = "진단 점수", example = "85")
    @NotNull(message = "점수는 필수입니다.")
    @Min(value = 0, message = "점수는 0 이상이어야 합니다.")
    private Integer score;

    @Schema(description = "사용자의 문해력 수준", example = "BEGINNER")
    @NotNull(message = "사용자 수준은 필수입니다.")
    private User.Level userLevel;
}