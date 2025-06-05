package com.deepread.service;

import com.deepread.dto.response.MypageStatisticsDto;
import com.deepread.dto.response.UserCalendarDto;
import com.deepread.entity.UserCalendar;
import com.deepread.repository.QuizResultRepository;
import com.deepread.repository.UserCalendarRepository;
import com.deepread.repository.SummaryRepository;
import com.deepread.repository.ChatbotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserCalendarRepository userCalendarRepository;
    private final QuizResultRepository quizResultRepository;
    private final SummaryRepository summaryRepository;
    private final ChatbotLogRepository chatbotLogRepository;

    // 1. 최근 1주일 학습 캘린더
    public List<UserCalendarDto> getWeeklyCalendarStatistics(Long userId) {
        List<UserCalendar> calendarEntries = userCalendarRepository.findByUserId(userId);

        return calendarEntries.stream().map(entry -> {
            LocalDate date = entry.getUserDate();
            String contentTitle = entry.getContent() != null ? entry.getContent().getTitle() : "콘텐츠 없음";
            return new UserCalendarDto(date, date.getDayOfWeek(), contentTitle);
        }).collect(Collectors.toList());
    }

    // 2. 마이페이지 학습 통계
    public MypageStatisticsDto getUserLearningStatistics(Long userId) {
        int readContentCount = summaryRepository.findByUserId(userId).size();
        int quizCompletedCount = quizResultRepository.findByUserId(userId).size();
        int learnedWords = chatbotLogRepository.countDistinctWordsByUserId(userId);

        List<UserCalendar> calendarEntries = userCalendarRepository.findByUserId(userId);
        int streak = calculateStreak(calendarEntries);

        return MypageStatisticsDto.builder()
                .readContentCount(readContentCount)
                .quizCompletedCount(quizCompletedCount)
                .learnedWordCount(learnedWords)
                .streak(streak)
                .build();
    }

    private int calculateStreak(List<UserCalendar> entries) {
        LocalDate today = LocalDate.now();
        int streak = 0;

        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            boolean studied = entries.stream().anyMatch(entry -> date.equals(entry.getUserDate()));
            if (studied) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }
}