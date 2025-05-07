package com.deepread.service;

import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.repository.SummaryFeedbackRepository;
import com.deepread.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final SummaryFeedbackRepository summaryFeedbackRepository;

    // 사용자의 요약 저장
    public Summary submitSummary(Summary summary) {
        return summaryRepository.save(summary);
    }

    // 요약에 대한 AI 피드백 저장
    public SummaryFeedback getFeedback(SummaryFeedback feedback) {
        return summaryFeedbackRepository.save(feedback);
    }

    // 요약 및 피드백 조회 (선택 기능)
    public Optional<Summary> getSummaryById(Long id) {
        return summaryRepository.findById(id);
    }

    // summaryId로 Summary를 조회한 뒤, 해당 요약에 대한 피드백을 반환
    public Optional<SummaryFeedback> getFeedbackBySummaryId(Long summaryId) {
        return summaryRepository.findById(summaryId)
                .flatMap(summaryFeedbackRepository::findBySummary);
    }

}
