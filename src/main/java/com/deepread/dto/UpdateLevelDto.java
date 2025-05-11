package com.deepread.dto;

import com.deepread.entity.User;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class UpdateLevelDto {
    @NotNull
    private User.Level newLevel;
}
