package com.deepread.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class SummaryDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long contentId;

    @NotNull
    private String userSummary;
}
