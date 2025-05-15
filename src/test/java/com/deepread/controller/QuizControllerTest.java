package com.deepread.controller;

import com.deepread.entity.QuizResult;
import com.deepread.entity.User;
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
        QuizResult result = new QuizResult();
        result.setUser(new User());

        Mockito.when(quizService.saveQuizResult(any(QuizResult.class)))
                .thenReturn(result);

        mockMvc.perform(post("/api/quiz/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 퀴즈 결과 예외 처리")
    void saveQuizResult_invalid() throws Exception {
        QuizResult invalid = new QuizResult(); // user == null

        mockMvc.perform(post("/api/quiz/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("퀴즈 결과 또는 사용자 정보가 유효하지 않습니다."));
    }
}
