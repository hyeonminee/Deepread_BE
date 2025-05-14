package com.deepread.controller;

import com.deepread.dto.SummaryDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;
    private final EntityManager entityManager;


    // 요약 제출 → 요약 저장 + AI 피드백 생성 및 저장 → 피드백과 함께 응답
    @Operation(summary = "요약 제출 및 AI 피드백 반환", description = "요약을 저장하고 AI 피드백을 즉시 생성하여 함께 반환한다.")
    @ApiResponse(responseCode = "200", description = "요약 및 피드백 저장 완료")
    @PostMapping
    public ResponseEntity<?> submitSummary(@RequestBody SummaryDto dto) {
        // 1. 요약 저장
        Summary summary = new Summary();
        summary.setUser(entityManager.getReference(User.class, dto.getUserId()));
        summary.setContent(entityManager.getReference(Content.class, dto.getContentId()));
        summary.setUserSummary(dto.getUserSummary());

        Summary savedSummary = summaryService.submitSummary(summary);

        // 2. AI 피드백 생성 및 저장 (summaryId 기준)
        SummaryFeedback feedback = summaryService.getFeedbackBySummaryId(savedSummary.getId())
                .orElse(null); // 없을 경우 null 반환 (또는 Optional 처리)

        // 3. 요약 + 피드백 함께 반환
        return ResponseEntity.ok(Map.of(
                "summaryId", savedSummary.getId(),
                "userSummary", savedSummary.getUserSummary(),
                "feedback", feedback
        ));
    }

    // 요약 단독 조회
    @Operation(summary = "요약 조회", description = "요약 ID를 통해 저장된 요약을 조회한다.")
    @ApiResponse(responseCode = "200", description = "요약 반환")
    @GetMapping("/{id}")
    public Summary getSummary(@PathVariable Long id) {
        return summaryService.getSummaryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("요약을 찾을 수 없습니다."));
    }

    // AI 피드백 단독 조회
    @Operation(summary = "AI 피드백 조회", description = "요약 ID를 통해 AI 피드백을 조회한다.")
    @ApiResponse(responseCode = "200", description = "피드백 반환")
    @GetMapping("/{id}/feedback")
    public SummaryFeedback getFeedback(@PathVariable Long id) {
        return summaryService.getFeedbackBySummaryId(id)
                .orElseThrow(() -> new ResourceNotFoundException("피드백을 찾을 수 없습니다."));
    }

//    @Operation(summary = "AI 피드백 저장", description = "특정 요약에 대한 AI 피드백을 저장한다.")
//    @ApiResponse(responseCode = "200", description = "피드백 저장 완료")
//    @PostMapping("/{id}/feedback")
//    public SummaryFeedback saveFeedback(@PathVariable Long id, @RequestBody SummaryFeedback feedback) {
//        Summary summary = summaryService.getSummaryById(id)
//                .orElseThrow(() -> new RuntimeException("요약을 찾을 수 없습니다."));
//        feedback.setSummary(summary);
//        return summaryService.getFeedback(feedback);
//    }
}
