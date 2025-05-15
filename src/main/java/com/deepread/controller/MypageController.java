package com.deepread.controller;

import com.deepread.dto.response.MypageStatisticsDto;
import com.deepread.dto.response.UserCalendarDto;
import com.deepread.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @Operation(summary = "주별 접속일 수 및 일별 콘텐츠", description = "사용자의 주별 접속일 수와 일별 조회 콘텐츠를 조회한다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/calendar/statistics/{userId}")
    public List<UserCalendarDto> getWeeklyCalendarStats(@PathVariable Long userId) {
        return mypageService.getWeeklyCalendarStatistics(userId);
    }

    @Operation(summary = "사용자별 요약/퀴즈 통계", description = "요약 피드백 평균 점수와 퀴즈 정확도를 반환한다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/statistics/{userId}")
    public MypageStatisticsDto getUserStatistics(@PathVariable Long userId) {
        return mypageService.getUserStatistics(userId);
    }

}
