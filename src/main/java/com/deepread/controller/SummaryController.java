package com.deepread.controller;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @Operation(summary = "요약 제출", description = "요약 텍스트를 제출하고 분석 결과를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<SummaryResponseDto> submitSummary(@RequestBody SummaryRequestDto dto) {
        SummaryResponseDto responseDto = summaryService.submitSummary(dto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "요약 단건 조회", description = "요약 ID를 기반으로 저장된 요약 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 조회 성공"),
            @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public Summary getSummary(@PathVariable Long id) {
        return summaryService.getSummaryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("요약을 찾을 수 없습니다."));
    }

    @Operation(summary = "요약 피드백 조회", description = "요약 ID를 기반으로 생성된 피드백을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드백 조회 성공"),
            @ApiResponse(responseCode = "404", description = "피드백을 찾을 수 없음")
    })
    @GetMapping("/{id}/feedback")
    public SummaryFeedback getFeedback(@PathVariable Long id) {
        return summaryService.getFeedbackBySummaryId(id)
                .orElseThrow(() -> new ResourceNotFoundException("피드백을 찾을 수 없습니다."));
    }
}
