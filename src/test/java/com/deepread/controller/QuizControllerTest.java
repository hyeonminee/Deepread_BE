package com.deepread.controller;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("퀴즈 결과 저장 성공")
    void saveQuizResult_success() throws Exception {
        // given
        QuizResultRequestDto requestDto = new QuizResultRequestDto();
        requestDto.setUserId(1L);
        requestDto.setTotalQuestions(5);
        requestDto.setCorrectCount(3);
        requestDto.setAccuracy(60.0f);

        QuizResultResponseDto responseDto = new QuizResultResponseDto();
        responseDto.setUserId(1L);
        responseDto.setTotalQuestions(5);
        responseDto.setCorrectCount(3);
        responseDto.setAccuracy(60.0f);

        // when
        Mockito.when(quizService.saveQuizResult(any(QuizResultRequestDto.class))).thenReturn(responseDto);

        // then
        mockMvc.perform(post("/api/quiz/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.totalQuestions").value(5))
                .andExpect(jsonPath("$.correctCount").value(3))
                .andExpect(jsonPath("$.accuracy").value(60.0));
    }

    @Test
    @DisplayName("퀴즈 결과 저장 실패 - 누락된 사용자 ID")
    void saveQuizResult_invalidRequest() throws Exception {
        QuizResultRequestDto invalidDto = new QuizResultRequestDto();

        mockMvc.perform(post("/api/quiz/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}