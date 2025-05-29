package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserCalendarDto {

    @Schema(description = "학습 날짜", example = "2025-05-25")
    private LocalDate date;

    @Schema(description = "요일", example = "MONDAY")
    private DayOfWeek dayOfWeek;

    @Schema(description = "해당 날짜에 본 콘텐츠 제목", example = "기후 변화의 영향")
    private String contentTitle;
}
