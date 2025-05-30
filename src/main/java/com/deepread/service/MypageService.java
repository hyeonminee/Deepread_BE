package com.deepread.service;

import com.deepread.dto.response.MypageStatisticsDto;
import com.deepread.dto.response.UserCalendarDto;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.QuizResult;
import com.deepread.entity.UserCalendar;
import com.deepread.repository.QuizResultRepository;
import com.deepread.repository.SummaryFeedbackRepository;
import com.deepread.repository.UserCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
        import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserCalendarRepository userCalendarRepository;
    private final QuizResultRepository quizResultRepository;
    private final SummaryFeedbackRepository summaryFeedbackRepository;

    public List<UserCalendarDto> getWeeklyCalendarStatistics(Long userId) {
        List<UserCalendar> calendarEntries = userCalendarRepository.findByUserId(userId);

        return calendarEntries.stream().map(entry -> {
            LocalDate date = entry.getUserDate();
            String contentTitle = entry.getContent() != null ? entry.getContent().getTitle() : "콘텐츠 없음";
            return new UserCalendarDto(date, date.getDayOfWeek(), contentTitle);
        }).collect(Collectors.toList());
    }

    public MypageStatisticsDto getUserStatistics(Long userId) {
        List<SummaryFeedback> feedbackList = summaryFeedbackRepository.findAll().stream()
                .filter(fb -> fb.getSummary().getUser().getId().equals(userId))
                .toList();

        List<QuizResult> quizResults = quizResultRepository.findByUserId(userId);

        float avgSummaryScore = (float) feedbackList.stream()
                .map(SummaryFeedback::getScore)
                .filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);

        float avgQuizAccuracy = (float) quizResults.stream()
                .map(QuizResult::getAccuracy)
                .filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);

        return new MypageStatisticsDto((float) avgSummaryScore, (float) avgQuizAccuracy);
    }
}
