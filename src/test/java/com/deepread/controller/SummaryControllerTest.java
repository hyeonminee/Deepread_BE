package com.deepread.controller;

import com.deepread.dto.request.SummaryRequestDto;
import com.deepread.dto.response.SummaryFeedbackResponseDto;
import com.deepread.dto.response.SummaryResponseDto;
import com.deepread.entity.SummaryFeedback;
import com.deepread.service.SummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SummaryController.class)
class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SummaryService summaryService;

    @Autowired
    private ObjectMapper objectMapper;

    private void setAuthenticatedUser(Long id, String name) {
        com.deepread.entity.User user = new com.deepread.entity.User();
        user.setId(id);
        user.setName(name);

        // ✅ ROLE_USER 권한 명시적으로 부여
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    void setUp() {
        setAuthenticatedUser(1L, "테스트 유저");
    }

    @Test
    @DisplayName("요약 제출 시 피드백 포함 응답 성공")
    void submitSummary_withFeedback_success() throws Exception {
        // given
        SummaryRequestDto dto = new SummaryRequestDto();
        dto.setUserId(1L);
        dto.setContentId(2L);
        dto.setUserSummary("테스트 요약입니다.");

        SummaryFeedbackResponseDto feedbackDto = new SummaryFeedbackResponseDto();
        feedbackDto.setScore(88.5f);
        feedbackDto.setFeedbackText("훌륭한 요약입니다.");

        SummaryResponseDto responseDto = new SummaryResponseDto();
        responseDto.setSummaryId(100L);
        responseDto.setUserSummary("테스트 요약입니다.");
        responseDto.setFeedback(feedbackDto);

        Mockito.when(summaryService.submitSummary(any(SummaryRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/summaries")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
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
