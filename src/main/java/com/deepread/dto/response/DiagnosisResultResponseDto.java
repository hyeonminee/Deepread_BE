package com.deepread.dto.response;

import com.deepread.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiagnosisResultResponseDto {
    private Long id;
    private Long userId;
    private Integer score;
    private User.Level userLevel;
    private LocalDateTime createdAt;
}
