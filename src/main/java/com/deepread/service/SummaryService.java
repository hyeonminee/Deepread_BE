package com.deepread.service;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.SummaryFeedbackResponseDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.SummaryFeedbackRepository;
import com.deepread.repository.SummaryRepository;
import com.deepread.repository.UserRepository;
import com.deepread.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final SummaryFeedbackRepository summaryFeedbackRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;
    private final SummaryEvaluationService summaryEvaluationService; // Flask 평가 서비스 의존성 주입

    // 요약 저장 + AI 평가 + DB 저장 + 응답 DTO 생성
    public SummaryResponseDto submitSummary(SummaryRequestDto dto) {
        // 1. 사용자 및 콘텐츠 검증
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Content content = contentRepository.findById(dto.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("콘텐츠를 찾을 수 없습니다."));

        // 2. Summary 객체 생성 및 저장
        Summary summary = new Summary();
        summary.setUser(user);
        summary.setContent(content);
        summary.setUserSummary(dto.getUserSummary());

        Summary saved = summaryRepository.save(summary);

        // 3. Flask 평가 결과를 기반으로 Feedback 생성 및 저장
        SummaryFeedback feedback = generateFeedback(saved);
        summaryFeedbackRepository.save(feedback);

        // 4. 클라이언트 응답용 DTO 생성
        SummaryResponseDto responseDto = new SummaryResponseDto();
        responseDto.setSummaryId(saved.getId());
        responseDto.setUserSummary(saved.getUserSummary());
        responseDto.setFeedback(modelMapper.map(feedback, SummaryFeedbackResponseDto.class));
        responseDto.setEvaluationResult(Map.of(
                "entailment_score", feedback.getScore(),
                "feedback", feedback.getFeedbackText()
        ));

        return responseDto;
    }

    // Flask 평가 결과로부터 SummaryFeedback 생성
    private SummaryFeedback generateFeedback(Summary summary) {
        String originalText = summary.getContent().getContent(); // 원문 텍스트
        String summaryText = summary.getUserSummary(); // 요약 텍스트

        Map<String, Object> result = summaryEvaluationService.evaluateSummary(originalText, summaryText);

        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setSummary(summary);
        feedback.setScore(((Number) result.get("entailment_score")).floatValue()); // 점수
        feedback.setFeedbackText((String) result.get("feedback")); // 피드백 문장
        return feedback;
    }

    // 단건 요약 조회
    public Optional<Summary> getSummaryById(Long id) {
        return summaryRepository.findById(id);
    }

    // 단건 피드백 조회
    public Optional<SummaryFeedback> getFeedbackBySummaryId(Long summaryId) {
        return summaryRepository.findById(summaryId)
                .flatMap(summaryFeedbackRepository::findBySummary);
    }
}
