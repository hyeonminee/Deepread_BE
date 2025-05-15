package com.deepread.controller;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping
    public ResponseEntity<SummaryResponseDto> submitSummary(@RequestBody SummaryRequestDto dto) {
        SummaryResponseDto responseDto = summaryService.submitSummary(dto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public Summary getSummary(@PathVariable Long id) {
        return summaryService.getSummaryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("요약을 찾을 수 없습니다."));
    }

    @GetMapping("/{id}/feedback")
    public SummaryFeedback getFeedback(@PathVariable Long id) {
        return summaryService.getFeedbackBySummaryId(id)
                .orElseThrow(() -> new ResourceNotFoundException("피드백을 찾을 수 없습니다."));
    }
}
