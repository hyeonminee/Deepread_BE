package com.deepread.dto.request;

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

    @NotNull
    private String userSummary;
}
