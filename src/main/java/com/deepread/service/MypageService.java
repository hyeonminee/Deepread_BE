package com.deepread.service;

import com.deepread.entity.UserCalendar;
import com.deepread.entity.UserReport;
import com.deepread.repository.UserCalendarRepository;
import com.deepread.repository.UserReportRepository;
import com.deepread.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserCalendarRepository userCalendarRepository;
    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;

    // 캘린더 기록 조회
    public List<UserCalendar> getUserCalendar(Long userId) {
        return userCalendarRepository.findByUserId(userId);
    }

    // 월별 리포트 조회
    public List<UserReport> getUserReport(Long userId) {
        return userReportRepository.findByUserId(userId);
    }

    // 회원 탈퇴 처리
    @Transactional
    public boolean withdrawUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setIsDeleted(true);
            return true;
        }).orElse(false);
    }
}
