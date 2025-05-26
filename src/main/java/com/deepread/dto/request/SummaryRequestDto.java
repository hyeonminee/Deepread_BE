package com.deepread.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long contentId;

    @NotBlank(message = "요약문을 입력해주세요.")
    private String userSummary;
}
