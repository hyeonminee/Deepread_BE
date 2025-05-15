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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final SummaryFeedbackRepository summaryFeedbackRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;

    public SummaryResponseDto submitSummary(SummaryRequestDto dto) {
        // 사용자, 콘텐츠 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Content content = contentRepository.findById(dto.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("콘텐츠를 찾을 수 없습니다."));

        // Summary Entity 생성
        Summary summary = new Summary();
        summary.setUser(user);
        summary.setContent(content);
        summary.setUserSummary(dto.getUserSummary());

        // Summary 저장
        Summary saved = summaryRepository.save(summary);

        // AI 피드백 생성 및 저장
        SummaryFeedback feedback = generateFeedback(saved);
        summaryFeedbackRepository.save(feedback);

        // DTO 응답 생성
        SummaryResponseDto responseDto = new SummaryResponseDto();
        responseDto.setSummaryId(saved.getId());
        responseDto.setUserSummary(saved.getUserSummary());
        responseDto.setFeedback(modelMapper.map(feedback, SummaryFeedbackResponseDto.class));

        return responseDto;
    }

    private SummaryFeedback generateFeedback(Summary summary) {
        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setSummary(summary);
        feedback.setScore(92.5f);
        feedback.setFeedbackText("핵심이 잘 드러난 요약입니다. 불필요한 반복이 없고 구조가 명확합니다.");
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
