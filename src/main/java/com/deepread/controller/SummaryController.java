package com.deepread.controller;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.LiteracyReportDto;
import com.deepread.dto.response.SummaryFeedbackResponseDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.dto.response.SummarySimpleDto;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Summary", description = "요약 관련 API (저장, 조회, 피드백, 날짜별 검색 등)"
)
@RestController
@RequestMapping("/api/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "요약 저장", description = "사용자가 작성한 요약을 저장하고 AI 평가를 수행함")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요약 저장 및 평가 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SummaryResponseDto> saveSummary(@RequestBody SummaryRequestDto requestDto) {
        return ResponseEntity.ok(summaryService.saveSummary(requestDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "요약 단건 조회", description = "요약 ID로 상세 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "요약 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SummaryResponseDto> getSummary(@PathVariable Long id) {
        Summary summary = summaryService.getSummaryEntity(id);
        SummaryResponseDto dto = modelMapper.map(summary, SummaryResponseDto.class);
        dto.setContentSnapshot(null); // 원문 제거
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/feedback/{summaryId}")
    @Operation(summary = "요약 피드백 조회", description = "요약 ID로 AI 피드백 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 조회 성공"),
            @ApiResponse(responseCode = "404", description = "피드백이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SummaryFeedbackResponseDto> getFeedback(@PathVariable Long summaryId) {
        return summaryService.getFeedbackBySummaryId(summaryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date/{userId}")
    @Operation(summary = "날짜별 요약 조회", description = "특정 날짜에 사용자가 작성한 요약 목록 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "날짜 파싱 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<List<SummarySimpleDto>> getSummariesByDate(
            @PathVariable Long userId,
            @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        List<SummarySimpleDto> summaries = summaryService.getSummariesByDate(userId, date);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/summary/{id}/original")
    @Operation(summary = "요약 원문 스냅샷 조회", description = "요약 당시 저장된 원문 내용을 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "원문 조회 성공"),
            @ApiResponse(responseCode = "404", description = "요약 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<String> getOriginalContent(@PathVariable Long id) {
        return ResponseEntity.ok(summaryService.getOriginalContent(id));
    }
}
