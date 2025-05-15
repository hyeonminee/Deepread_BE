package com.deepread.controller;

import com.deepread.entity.ChatbotLog;
import com.deepread.entity.User;
import com.deepread.service.ChatbotService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatbotController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatbotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatbotService chatbotService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("챗봇 로그 저장 성공")
    void saveChatLog_success() throws Exception {
        ChatbotLog log = new ChatbotLog();
        log.setUser(new User());
        log.setQuestion("질문입니다.");
        log.setResponse("응답입니다.");

        Mockito.when(chatbotService.saveChatbotLog(any(ChatbotLog.class)))
                .thenReturn(log);

        mockMvc.perform(post("/api/chatbot/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(log)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 챗봇 로그 예외 처리")
    void saveChatLog_invalid() throws Exception {
        ChatbotLog invalid = new ChatbotLog(); // user, question 없음

        mockMvc.perform(post("/api/chatbot/log")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("챗봇 로그 또는 사용자 정보가 유효하지 않습니다."));
    }
}
