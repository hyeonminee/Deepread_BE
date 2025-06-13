package com.deepread.service;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.LiteracyReportDto;
import com.deepread.dto.response.SummaryFeedbackResponseDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.dto.response.SummarySimpleDto;
import com.deepread.entity.*;
import com.deepread.exception.CustomEvaluationException;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
    private final RestTemplate restTemplate;

    // Flask 기반 AI 평가 서버 URL
    @Value("${flask.api.url}")
    private String flaskBaseUrl;

    /**
     * 사용자의 요약 결과를 저장하는 메서드.
     * - Content 테이블에서 카테고리와 외부 ID를 기반으로 실제 원문과 AI 요약을 불러옴
     * - Flask AI 평가 서버로 원문/사용자 요약/AI 요약을 POST 전송하여 score 및 feedback 수신
     * - Summary 엔티티로 DB에 저장
     * - 결과를 DTO로 변환하여 반환
     */
    @Transactional
    public SummaryResponseDto saveSummary(SummaryRequestDto dto) {
        // 0. 사용자 조회 (userId로 User 객체 조회)
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 1. 콘텐츠 ID로 Content 엔티티 조회 (LAW, MEDICAL, NEWS 중 하나)
        Content content = contentRepository.findById(dto.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 콘텐츠를 찾을 수 없습니다."));

        // 2. Content의 카테고리 및 externalId에 따라 원문과 AI 요약을 외부 테이블에서 가져옴
        String originalText;
        String aiSummary;

        switch (content.getCategory().toUpperCase()) {
            case "LAW" -> {
                LawArticle article = lawArticleRepository.findById(content.getExternalId())
                        .orElseThrow(() -> new ResourceNotFoundException("해당 법률 문서를 찾을 수 없습니다."));
                originalText = article.getContent();
                aiSummary = article.getAiSummary();
            }
            case "MEDICAL" -> {
                MedicalArticle article = medicalArticleRepository.findById(content.getExternalId())
                        .orElseThrow(() -> new ResourceNotFoundException("해당 의료 문서를 찾을 수 없습니다."));
                originalText = article.getContent();
                aiSummary = article.getAiSummary();
            }
            case "NEWS" -> {
                NewsArticle article = newsArticleRepository.findById(content.getExternalId())
                        .orElseThrow(() -> new ResourceNotFoundException("해당 뉴스 기사를 찾을 수 없습니다."));
                originalText = article.getContent();
                aiSummary = article.getAiSummary();
            }
            default -> throw new IllegalArgumentException("알 수 없는 콘텐츠 카테고리입니다.");
        }

        // 3. AI 평가 서버로 보낼 요청 데이터를 Map 형태로 구성
        Map<String, String> payload = new HashMap<>();
        payload.put("text", originalText);                    // 원문
        payload.put("user_summary", dto.getUserSummary());    // 사용자 요약
        payload.put("ai_summary", aiSummary);                 // AI 요약

        // 4. HTTP 요청 구성 (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);

        // 5. Flask AI 평가 서버에 POST 요청 전송
        ResponseEntity<Map> response = restTemplate.postForEntity(flaskBaseUrl + "/evaluate", requestEntity, Map.class);

        // 6. 응답 결과(score, feedback) 파싱
        Double score;
        String feedback;
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object scoreObj = response.getBody().get("score");
            if (!(scoreObj instanceof Number)) {
                throw new CustomEvaluationException("AI 평가 서버에서 score가 Number 타입이 아님: " + scoreObj);
            }
            score = ((Number) scoreObj).doubleValue();
            feedback = (String) response.getBody().get("feedback");
        } else {
            throw new CustomEvaluationException("AI 평가 서버 응답 오류: " + response.getStatusCode());
        }

        // 7. Summary 엔티티 생성 및 저장
        Summary summary = Summary.builder()
                .user(user)
                .content(content)
                .userSummary(dto.getUserSummary())
                .aiSummary(aiSummary)
                .contentSnapshot(originalText) // 당시 원문 복사
                .score(score.doubleValue())  // DB는 Double 타입이므로 형변환
                .feedback(feedback)
                .createdAt(LocalDateTime.now()) // 명시적 설정 필요
                .build();

        summaryRepository.save(summary);

        // 8. 저장된 결과를 DTO로 변환하여 반환
        SummaryResponseDto responseDto = modelMapper.map(summary, SummaryResponseDto.class);
        responseDto.setContentSnapshot(null); // 프론트 요청에 따라 제거
        return responseDto;
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


    // Summary ID로부터 평가 피드백만 반환
    public Optional<SummaryFeedbackResponseDto> getFeedbackBySummaryId(Long summaryId) {
        return summaryRepository.findById(summaryId)
                .map(summary -> new SummaryFeedbackResponseDto(
                        summary.getScore().floatValue(),
                        summary.getFeedback()
                ));
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

    // Summary ID로부터 원문만 반환 (contentSnapshot)
    public String getOriginalContent(Long id) {
        return summaryRepository.findById(id)
                .map(Summary::getContentSnapshot)
                .orElseThrow(() -> new IllegalArgumentException("요약 ID를 찾을 수 없습니다."));
    }

    // Summary 엔티티 직접 반환
    public Summary getSummaryEntity(Long summaryId) {
        return summaryRepository.findById(summaryId)
                .orElseThrow(() -> new ResourceNotFoundException("요약 ID를 찾을 수 없습니다."));
    }
}
