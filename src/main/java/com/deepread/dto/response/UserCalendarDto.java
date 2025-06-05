package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCalendarDto {

    @Schema(description = "학습한 날짜", example = "2025-06-05")
    private LocalDate date;

    @Schema(description = "요일", example = "THURSDAY")
    private DayOfWeek dayOfWeek;

    @Schema(description = "학습한 콘텐츠 제목", example = "최저임금법 개정안")
    private String contentTitle;
}
