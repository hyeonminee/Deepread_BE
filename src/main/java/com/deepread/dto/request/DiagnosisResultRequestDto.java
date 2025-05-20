package com.deepread.dto.request;

import com.deepread.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisResultRequestDto {

    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @NotNull(message = "점수는 필수입니다.")
    @Min(value = 0, message = "점수는 0 이상이어야 합니다.")
    private Integer score;

    @NotNull(message = "사용자 수준은 필수입니다.")
    private User.Level userLevel;
}