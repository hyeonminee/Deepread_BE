package com.deepread.controller;

import com.deepread.entity.UserReport;
import com.deepread.exception.ResourceNotFoundException;
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

    @Operation(summary = "캘린더 조회", description = "사용자의 월별 학습 일자를 조회한다.")
    @ApiResponse(responseCode = "200", description = "캘린더 정보 반환")
    @GetMapping("/calendar/{userId}")
    public List<String> getUserCalendar(@PathVariable Long userId) {
        return mypageService.getUserCalendar(userId).stream()
                .map(calendar -> calendar.getUser_date().toString())
                .toList();
    }

    @Operation(summary = "리포트 조회", description = "사용자의 월별 학습 리포트를 조회한다.")
    @ApiResponse(responseCode = "200", description = "리포트 반환")
    @GetMapping("/report/{userId}")
    public List<UserReport> getUserReport(@PathVariable Long userId) {
        List<UserReport> reports = mypageService.getUserReport(userId);
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("리포트를 찾을 수 없습니다.");
        }
        return reports;
    }

    @Operation(summary = "회원 탈퇴", description = "사용자의 계정을 삭제한다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공 여부 반환")
    @DeleteMapping("/withdraw/{userId}")
    public boolean withdrawUser(@PathVariable Long userId) {
        return mypageService.withdrawUser(userId);
    }
}
