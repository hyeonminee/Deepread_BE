package com.deepread.controller;

import com.deepread.dto.SummaryDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.service.SummaryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import com.deepread.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;
    private final EntityManager entityManager;

    // 사용자의 요약 제출 => 사용자가 작성한 요약 저장
    @PostMapping
    public Summary submitSummary(@RequestBody SummaryDto dto) {
        // dto: 사용자가 작성한 요약
        Summary summary = new Summary();
        summary.setUser(entityManager.getReference(User.class, dto.getUserId()));
        summary.setContent(entityManager.getReference(Content.class, dto.getContentId()));
        summary.setUserSummary(dto.getUserSummary());

        return summaryService.submitSummary(summary);
    }

    // 특정 요약 조회
    @GetMapping("/{id}")
    public Summary getSummary(@PathVariable Long id) {
        return summaryService.getSummaryById(id)
                .orElseThrow(() -> new RuntimeException("요약을 찾을 수 없습니다."));
    }

    // AI 피드백 저장 (요약 ID에 연결)
    @PostMapping("/{id}/feedback")
    public SummaryFeedback saveFeedback(@PathVariable Long id, @RequestBody SummaryFeedback feedback) {
        Summary summary = summaryService.getSummaryById(id)
                .orElseThrow(() -> new RuntimeException("요약을 찾을 수 없습니다."));
        feedback.setSummary(summary);

        return summaryService.getFeedback(feedback);
    }

    // AI 피드백 조회
    @GetMapping("/{id}/feedback")
    public SummaryFeedback saveFeedback(@PathVariable Long id) {
        return summaryService.getFeedbackBySummaryId(id)
                .orElseThrow(() -> new RuntimeException("피드백을 찾을 수 없습니다."));
    }
}
