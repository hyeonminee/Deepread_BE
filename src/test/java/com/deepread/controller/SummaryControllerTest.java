package com.deepread.controller;

import com.deepread.dto.SummaryDto;
import com.deepread.entity.Content;
import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.entity.User;
import com.deepread.service.SummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SummaryController.class)
class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SummaryService summaryService;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private void setAuthenticatedUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    void setUp() {
        setAuthenticatedUser(1L, "테스트 유저");
    }

    @Test
    @DisplayName("요약 제출 시 피드백 포함 응답 성공")
    void submitSummary_withFeedback_success() throws Exception {
        // Given
        SummaryDto dto = new SummaryDto();
        dto.setUserId(1L);
        dto.setContentId(2L);
        dto.setUserSummary("테스트 요약입니다.");

        Summary savedSummary = new Summary();
        savedSummary.setId(100L);
        savedSummary.setUserSummary(dto.getUserSummary());

        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setScore(88.5f);
        feedback.setFeedbackText("훌륭한 요약입니다.");

        // Mock
        when(entityManager.getReference(User.class, 1L)).thenReturn(new User());
        when(entityManager.getReference(Content.class, 2L)).thenReturn(new Content());
        when(summaryService.submitSummary(any(Summary.class))).thenReturn(savedSummary);
        when(summaryService.getFeedbackBySummaryId(100L)).thenReturn(Optional.of(feedback));

        // When & Then
        mockMvc.perform(post("/api/summaries")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryId").value(100))
                .andExpect(jsonPath("$.userSummary").value("테스트 요약입니다."))
                .andExpect(jsonPath("$.feedback.score").value(88.5))
                .andExpect(jsonPath("$.feedback.feedbackText").value("훌륭한 요약입니다."));
    }

    @Test
    @DisplayName("AI 피드백 조회 성공")
    void getFeedback_success() throws Exception {
        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setScore(90f);
        feedback.setFeedbackText("피드백 조회 성공");

        Mockito.when(summaryService.getFeedbackBySummaryId(1L)).thenReturn(Optional.of(feedback));

        mockMvc.perform(get("/api/summaries/1/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackText").value("피드백 조회 성공"))
                .andExpect(jsonPath("$.score").value(90.0));
    }
}
