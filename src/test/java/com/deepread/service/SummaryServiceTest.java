package com.deepread.service;

import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.repository.SummaryFeedbackRepository;
import com.deepread.repository.SummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummaryServiceTest {

    @Mock
    private SummaryRepository summaryRepository;

    @Mock
    private SummaryFeedbackRepository summaryFeedbackRepository;

    @InjectMocks
    private SummaryService summaryService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitSummary_success() {
        Summary summary = new Summary();
        when(summaryRepository.save(any())).thenReturn(summary);

        Summary result = summaryService.submitSummary(summary);

        assertNotNull(result);
        verify(summaryRepository).save(summary);
    }

    @Test
    void submitSummary_shouldAlsoGenerateFeedback() {
        Summary summary = new Summary();
        // 필요한 mock 구성
        when(summaryRepository.save(any())).thenReturn(summary);
        when(summaryFeedbackRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Summary result = summaryService.submitSummary(summary);

        assertNotNull(result);
        verify(summaryFeedbackRepository, times(1)).save(any(SummaryFeedback.class));
    }

}