package com.deepread.controller;

import com.deepread.dto.response.MypageStatisticsDto;
import com.deepread.dto.response.UserCalendarDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.dto.response.LiteracyReportDto;
import com.deepread.service.MypageService;
import com.deepread.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "MyPage", description = "마이페이지 관련 API")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final SummaryService summaryService;
    private final MypageService mypageService;

    @Operation(summary = "요약 상세 조회", description = "요약 ID로 상세 정보(userSummary, aiSummary 등) 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "요약 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/summary/{id}")
    public SummaryResponseDto getSummaryDetail(@PathVariable Long id) {
        return summaryService.getSummary(id);
    }

    @Operation(summary = "요약 원문 보기", description = "요약 ID로 원문 스냅샷 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "요약 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/summary/{id}/original")
    public String getOriginalContent(@PathVariable Long id) {
        return summaryService.getOriginalContent(id);
    }

    @Operation(summary = "문해력 리포트", description = "사용자의 평균 점수, 최고/최저, 총 요약 수 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리포트 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/literacy-report/{userId}")
    public LiteracyReportDto getLiteracyReport(@PathVariable Long userId,
                                               @RequestParam(name = "date", required = false) String date) {
        LocalDate reportDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        return summaryService.getLiteracyReport(userId, reportDate);
    }

    @Operation(summary = "주간 학습 캘린더", description = "최근 일주일 간의 학습 콘텐츠 활동 정보를 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/calendar/week/{userId}")
    public List<UserCalendarDto> getWeeklyCalendar(@PathVariable Long userId) {
        return mypageService.getWeeklyCalendarStatistics(userId);
    }

    @Operation(summary = "마이페이지 통계", description = "누적 콘텐츠, 퀴즈, 단어 수, 연속 학습일을 포함한 학습 통계를 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/statistics/{userId}")
    public MypageStatisticsDto getUserStatistics(@PathVariable Long userId) {
        return mypageService.getUserLearningStatistics(userId);
    }
}
