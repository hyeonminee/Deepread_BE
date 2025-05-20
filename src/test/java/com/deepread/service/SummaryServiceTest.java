package com.deepread.service;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.SummaryFeedbackResponseDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.ContentRepository;
import com.deepread.repository.SummaryFeedbackRepository;
import com.deepread.repository.SummaryRepository;
import com.deepread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummaryServiceTest {

    private SummaryRepository summaryRepository;
    private SummaryFeedbackRepository summaryFeedbackRepository;
    private UserRepository userRepository;
    private ContentRepository contentRepository;
    private SummaryService summaryService;

    @BeforeEach
    void setUp() {
        summaryRepository = mock(SummaryRepository.class);
        summaryFeedbackRepository = mock(SummaryFeedbackRepository.class);
        userRepository = mock(UserRepository.class);
        contentRepository = mock(ContentRepository.class);
        summaryService = new SummaryService(
                summaryRepository,
                summaryFeedbackRepository,
                userRepository,
                contentRepository,
                new ModelMapper()
        );
    }

    @Test
    @DisplayName("요약 제출 및 피드백 생성 성공")
    void submitSummary_success() {
        // given
        Long userId = 1L;
        Long contentId = 2L;
        String userSummary = "사용자 요약입니다.";

        SummaryRequestDto dto = new SummaryRequestDto();
        dto.setUserId(userId);
        dto.setContentId(contentId);
        dto.setUserSummary(userSummary);

        User user = new User();
        user.setId(userId);

        Content content = new Content();
        content.setId(contentId);

        Summary savedSummary = new Summary();
        savedSummary.setId(10L);
        savedSummary.setUser(user);
        savedSummary.setContent(content);
        savedSummary.setUserSummary(userSummary);

        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setSummary(savedSummary);
        feedback.setScore(92.5f);
        feedback.setFeedbackText("좋은 요약입니다.");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
        when(summaryRepository.save(any(Summary.class))).thenReturn(savedSummary);
        when(summaryFeedbackRepository.save(any(SummaryFeedback.class))).thenReturn(feedback);

        // when
        SummaryResponseDto responseDto = summaryService.submitSummary(dto);

        // then
        assertNotNull(responseDto);
        assertEquals(10L, responseDto.getSummaryId());
        assertEquals("사용자 요약입니다.", responseDto.getUserSummary());
        assertNotNull(responseDto.getFeedback());
        assertEquals(92.5f, responseDto.getFeedback().getScore());
        assertEquals("핵심이 잘 드러난 요약입니다. 불필요한 반복이 없고 구조가 명확합니다.", responseDto.getFeedback().getFeedbackText());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 예외 발생")
    void submitSummary_userNotFound() {
        SummaryRequestDto dto = new SummaryRequestDto();
        dto.setUserId(999L);
        dto.setContentId(1L);
        dto.setUserSummary("내용");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            summaryService.submitSummary(dto);
        });
    }

    @Test
    @DisplayName("존재하지 않는 콘텐츠 예외 발생")
    void submitSummary_contentNotFound() {
        SummaryRequestDto dto = new SummaryRequestDto();
        dto.setUserId(1L);
        dto.setContentId(999L);
        dto.setUserSummary("내용");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(contentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            summaryService.submitSummary(dto);
        });
    }

    @Test
    @DisplayName("요약 ID로 Summary 조회 성공")
    void getSummaryById_success() {
        Summary summary = new Summary();
        summary.setId(1L);

        when(summaryRepository.findById(1L)).thenReturn(Optional.of(summary));

        Optional<Summary> result = summaryService.getSummaryById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("요약 ID로 피드백 조회 성공")
    void getFeedbackBySummaryId_success() {
        Summary summary = new Summary();
        summary.setId(1L);

        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setFeedbackText("좋습니다.");
        feedback.setScore(95f);

        when(summaryRepository.findById(1L)).thenReturn(Optional.of(summary));
        when(summaryFeedbackRepository.findBySummary(summary)).thenReturn(Optional.of(feedback));

        Optional<SummaryFeedback> result = summaryService.getFeedbackBySummaryId(1L);

        assertTrue(result.isPresent());
        assertEquals("좋습니다.", result.get().getFeedbackText());
    }
}
