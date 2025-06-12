package com.deepread.dto.response;

import com.deepread.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisEvaluationResponseDto {

    @Schema(description = "총점 (100점 만점)", example = "85")
    private Integer score;

    @Schema(description = "사용자의 문해력 수준", example = "고급")
    private User.Level userLevel;
}
