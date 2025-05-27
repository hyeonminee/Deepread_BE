package com.deepread.controller;

import com.deepread.dto.response.MypageStatisticsDto;
import com.deepread.dto.response.UserCalendarDto;
import com.deepread.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mypage", description = "마이페이지 관련 통계 API")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @Operation(
            summary = "주별 접속일 수 및 일별 콘텐츠 조회",
            description = "사용자의 최근 일주일 간의 접속 일수와 매일 조회한 콘텐츠 정보를 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/calendar/statistics/{userId}")
    public List<UserCalendarDto> getWeeklyCalendarStats(
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @PathVariable Long userId
    ) {
        return mypageService.getWeeklyCalendarStatistics(userId);
    }

    @Operation(
            summary = "요약 및 퀴즈 통계 조회",
            description = "해당 사용자의 요약 피드백 평균 점수 및 퀴즈 정확도를 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "통계 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/statistics/{userId}")
    public MypageStatisticsDto getUserStatistics(
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @PathVariable Long userId
    ) {
        return mypageService.getUserStatistics(userId);
    }
}
