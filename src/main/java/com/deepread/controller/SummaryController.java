package com.deepread.controller;

import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    // 사용자의 요약 제출 => 사용자가 작성한 요약 저장
    @PostMapping
    public Summary submitSummary(@RequestBody Summary summary) {
        return summaryService.submitSummary(summary);
    }

    // AI 피드백 저장
    @PostMapping("/feedback")
    public SummaryFeedback saveFeedback(@RequestBody SummaryFeedback feedback) {
        return summaryService.getFeedback(feedback);
    }

    // 요약 ID로 요약 조회
    @GetMapping("/{id}")
    public Summary getSummaryById(@PathVariable Long id) {
        return summaryService.getSummaryById(id)
                .orElseThrow(() -> new RuntimeException("요약을 찾을 수 없습니다."));
    }

    // 요약 ID로 요약에 대한 AI 피드백 조회
    @GetMapping("/{id}/feedback")
    public SummaryFeedback getFeedbackBySummaryId(@PathVariable Long id) {
        return summaryService.getFeedbackBySummaryId(id)
                .orElseThrow(() -> new RuntimeException("피드백을 찾을 수 없습니다."));
    }
}
