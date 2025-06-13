package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.deepread.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 레벨 수정 요청 DTO")
public class UpdateLevelRequestDto {

    @Schema(description = "새로운 사용자 레벨", example = "ADVANCED")
    @NotNull(message = "새로운 레벨은 필수입니다.")
    private User.Level newLevel;
}