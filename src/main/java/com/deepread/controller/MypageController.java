package com.deepread.controller;

import com.deepread.entity.UserReport;
import com.deepread.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    // 캘린더 조회 (월별 학습 캘린더)
    @GetMapping("/calendar/{userId}")
    public List<String> getUserCalendar(@PathVariable Long userId) {
        return mypageService.getUserCalendar(userId).stream()
                .map(calendar -> calendar.getUser_date().toString())
                .toList();
    }

    // 월별 리포트 조회
    @GetMapping("/report/{userId}")
    public List<UserReport> getUserReport(@PathVariable Long userId) {
        return mypageService.getUserReport(userId);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw/{userId}")
    public boolean withdrawUser(@PathVariable Long userId) {
        return mypageService.withdrawUser(userId);
    }
}
