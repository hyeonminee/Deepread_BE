package com.deepread.dto.request;

import com.deepread.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisResultRequestDto {
    @NotNull
    private Long userId;

    private Integer score;

    private User.Level userLevel;
}
