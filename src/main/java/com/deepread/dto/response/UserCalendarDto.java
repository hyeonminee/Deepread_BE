package com.deepread.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserCalendarDto {
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private String contentTitle;
}
