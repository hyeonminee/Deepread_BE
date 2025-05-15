package com.deepread.controller;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
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
        ChatbotLogRequestDto requestDto = new ChatbotLogRequestDto();
        requestDto.setUserId(1L);
        requestDto.setQuestion("질문입니다.");
        requestDto.setResponse("응답입니다.");

        ChatbotLogResponseDto responseDto = new ChatbotLogResponseDto();
        responseDto.setUserId(1L);
        responseDto.setQuestion("질문입니다.");
        responseDto.setResponse("응답입니다.");

        Mockito.when(chatbotService.saveChatbotLog(any(ChatbotLogRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/chatbot/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.question").value("질문입니다."))
                .andExpect(jsonPath("$.response").value("응답입니다."));
    }

    @Test
    @DisplayName("잘못된 챗봇 로그 예외 처리")
    void saveChatLog_invalid() throws Exception {
        ChatbotLogRequestDto invalid = new ChatbotLogRequestDto();

        mockMvc.perform(post("/api/chatbot/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
