package com.deepread.dto.request;

import com.deepread.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자의 문해력 수준(Level)을 수정하기 위한 요청 DTO입니다.
 */
@Getter
@Setter
public class UpdateLevelRequestDto {

    @NotNull(message = "새로운 레벨은 필수입니다.")
    private User.Level newLevel;
}
