package com.deepread.service;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.SummaryFeedbackResponseDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.*;
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
    private final SummaryEvaluationService summaryEvaluationService;

    public SummaryResponseDto submitSummary(SummaryRequestDto dto) {
        // 사용자, 콘텐츠 가져오기
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Content content = contentRepository.findById(dto.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("콘텐츠를 찾을 수 없습니다."));

        // Summary 저장
        Summary summary = new Summary();
        summary.setUser(user);
        summary.setContent(content);
        summary.setUserSummary(dto.getUserSummary());

        Summary saved = summaryRepository.save(summary);

        // AI 평가 요청 및 결과 저장
        SummaryFeedback feedback = generateFeedback(saved);
        summaryFeedbackRepository.save(feedback);

        // 응답 DTO 구성
        SummaryResponseDto responseDto = new SummaryResponseDto();
        responseDto.setSummaryId(saved.getId());
        responseDto.setUserSummary(saved.getUserSummary());
        responseDto.setFeedback(modelMapper.map(feedback, SummaryFeedbackResponseDto.class));
        responseDto.setEvaluationResult(Map.of(
                "score", feedback.getScore(),
                "feedback", feedback.getFeedbackText(),
                "ai_summary", content.getAiSummary() // DB에 저장된 기준 요약
        ));

        return responseDto;
    }

    private SummaryFeedback generateFeedback(Summary summary) {
        String originalText = summary.getContent().getContent();
        String aiSummary = summary.getContent().getAiSummary(); // 기준 요약문
        String userSummary = summary.getUserSummary();

        Map<String, Object> result = summaryEvaluationService.evaluateSummary(originalText, aiSummary, userSummary);

        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setSummary(summary);
        feedback.setScore(((Number) result.get("score")).floatValue());
        feedback.setFeedbackText((String) result.get("feedback"));
        return feedback;
    }

    public Optional<Summary> getSummaryById(Long id) {
        return summaryRepository.findById(id);
    }

    public Optional<SummaryFeedback> getFeedbackBySummaryId(Long summaryId) {
        return summaryRepository.findById(summaryId)
                .flatMap(summaryFeedbackRepository::findBySummary);
    }
}
