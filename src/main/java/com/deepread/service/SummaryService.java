package com.deepread.service;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.LiteracyReportDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.dto.response.SummarySimpleDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.User;
import com.deepread.exception.CustomEvaluationException;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final SummaryFeedbackRepository feedbackRepository;
    private final QuizResultRepository quizResultRepository;

    private final LawArticleRepository lawArticleRepository;
    private final MedicalArticleRepository medicalArticleRepository;
    private final NewsArticleRepository newsArticleRepository;

    private final ModelMapper modelMapper;

    // 요약 저장 및 AI 평가
    @Transactional
    public SummaryResponseDto saveSummary(SummaryRequestDto requestDto) {
        log.info("요약 저장 요청 수신: userId={}, contentId={}", requestDto.getUserId(), requestDto.getContentId());

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {
                    log.error("사용자 ID {}를 찾을 수 없음", requestDto.getUserId());
                    return new ResourceNotFoundException("User not found");
                });

        Content content = contentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> {
                    log.error("콘텐츠 ID {}를 찾을 수 없음", requestDto.getContentId());
                    return new ResourceNotFoundException("Content not found");
                });

        String originalText = fetchOriginalText(content);
        String userSummary = requestDto.getUserSummary();
        String aiSummary = requestDto.getAiSummary();

        // AI 평가 요청
        RestTemplate restTemplate = new RestTemplate();
        String aiUrl = "http://3.35.200.173:5000/evaluate";

        Map<String, String> payload = new HashMap<>();
        payload.put("text", originalText);
        payload.put("ai_summary", aiSummary);
        payload.put("user_summary", userSummary);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(aiUrl, HttpMethod.POST, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (response.getStatusCode() != HttpStatus.OK || responseBody == null) {
            log.error("AI 평가 응답 실패: status={}, body={}", response.getStatusCode(), responseBody);
            throw new CustomEvaluationException("AI 평가에 실패했습니다. 다시 시도해 주세요.");
        }

        Double score = ((Number) responseBody.get("score")).doubleValue();
        String feedback = (String) responseBody.get("feedback");

        Summary summary = Summary.builder()
                .user(user)
                .content(content)
                .userSummary(userSummary)
                .aiSummary(aiSummary)
                .score(score)
                .feedback(feedback)
                .contentSnapshot(originalText)
                .createdAt(LocalDateTime.now())
                .build();

        Summary saved = summaryRepository.save(summary);
        log.info("요약 저장 완료: summaryId={}, score={}", saved.getId(), score);

        return modelMapper.map(saved, SummaryResponseDto.class);
    }

    private String fetchOriginalText(Content content) {
        String category = content.getCategory();
        Long externalId = content.getExternalId();

        return switch (category.toUpperCase()) {
            case "LAW" -> lawArticleRepository.findById(externalId)
                    .orElseThrow(() -> new ResourceNotFoundException("법률 문서를 찾을 수 없습니다.")).getContent();
            case "MEDICAL" -> medicalArticleRepository.findById(externalId)
                    .orElseThrow(() -> new ResourceNotFoundException("의료 정보를 찾을 수 없습니다.")).getContent();
            case "NEWS" -> newsArticleRepository.findById(externalId)
                    .orElseThrow(() -> new ResourceNotFoundException("뉴스 기사를 찾을 수 없습니다.")).getContent();
            default -> throw new IllegalArgumentException("알 수 없는 카테고리: " + category);
        };
    }

    // 요약 단건 조회
    public SummaryResponseDto getSummary(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> {
                    log.error("요약 ID {}를 찾을 수 없음", summaryId);
                    return new ResourceNotFoundException("Summary not found");
                });
        return modelMapper.map(summary, SummaryResponseDto.class);
    }

    // 요약 ID로 피드백 조회
    public Optional<SummaryFeedback> getFeedbackBySummaryId(Long summaryId) {
        return feedbackRepository.findBySummaryId(summaryId);
    }

    // 특정 날짜의 요약 리스트 반환
    public List<SummarySimpleDto> getSummariesByDate(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Summary> summaries = summaryRepository.findByUserIdAndCreatedAtBetween(userId, start, end);

        log.info("사용자 {}의 {} 날짜 요약 개수: {}", userId, date, summaries.size());

        return summaries.stream()
                .map(SummarySimpleDto::from)
                .collect(Collectors.toList());
    }

    // 문해력 리포트 조회
    public LiteracyReportDto getLiteracyReport(Long userId, LocalDate reportDate) {
        // 평가 구간 설정: 1일, 14일, 28일 기준
        LocalDate start;
        if (reportDate.getDayOfMonth() <= 13) {
            start = reportDate.withDayOfMonth(1);
        } else if (reportDate.getDayOfMonth() <= 27) {
            start = reportDate.withDayOfMonth(14);
        } else {
            start = reportDate.withDayOfMonth(28);
        }
        LocalDate end = start.plusDays(13);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();

        // 요약 점수 평균
        Double summaryAvgValue = summaryRepository.findAverageScoreByUserIdAndPeriod(userId, startDateTime, endDateTime);
        double summaryAvg = (summaryAvgValue != null) ? summaryAvgValue : 0.0;

        // 퀴즈 점수 평균
        Double quizAvgValue = quizResultRepository.findAverageScoreByUserIdAndPeriod(userId, startDateTime, endDateTime);
        double quizAvg = (quizAvgValue != null) ? quizAvgValue : 0.0;

        // 평균 합산
        double totalAvg = (summaryAvg + quizAvg) / 2;

        return LiteracyReportDto.builder()
                .startDate(start)
                .endDate(end)
                .summaryAvg(summaryAvg)
                .quizAvg(quizAvg)
                .totalAvg(totalAvg)
                .build();
    }

    // 요약 원문 스냅샷 조회
    public String getOriginalContent(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Summary not found"));
        return summary.getContentSnapshot();
    }
}
