package com.deepread.service;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
import com.deepread.entity.ChatbotLog;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.ChatbotLogRepository;
import com.deepread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatbotServiceTest {

    private ChatbotLogRepository chatbotLogRepository;
    private UserRepository userRepository;
    private ChatbotService chatbotService;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        chatbotLogRepository = mock(ChatbotLogRepository.class);
        userRepository = mock(UserRepository.class);
        modelMapper = new ModelMapper();
        chatbotService = new ChatbotService(chatbotLogRepository, userRepository, modelMapper);
    }

    @Test
    @DisplayName("챗봇 로그 저장 성공")
    void saveChatbotLog_success() {
        // given
        Long userId = 1L;
        ChatbotLogRequestDto dto = new ChatbotLogRequestDto();
        dto.setUserId(userId);
        dto.setQuestion("질문");
        dto.setResponse("응답");

        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ChatbotLog savedLog = new ChatbotLog();
        savedLog.setId(10L);
        savedLog.setUser(user);
        savedLog.setQuestion("질문");
        savedLog.setResponse("응답");

        when(chatbotLogRepository.save(any(ChatbotLog.class))).thenReturn(savedLog);

        // when
        ChatbotLogResponseDto response = chatbotService.saveChatbotLog(dto);

        // then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("질문", response.getQuestion());
        assertEquals("응답", response.getResponse());
        assertEquals(userId, response.getUserId());
        verify(chatbotLogRepository).save(any(ChatbotLog.class));
    }

    @Test
    @DisplayName("챗봇 로그 저장 실패 - 사용자 없음")
    void saveChatbotLog_userNotFound() {
        // given
        ChatbotLogRequestDto dto = new ChatbotLogRequestDto();
        dto.setUserId(999L);
        dto.setQuestion("테스트");
        dto.setResponse("응답");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> chatbotService.saveChatbotLog(dto));
        verify(chatbotLogRepository, never()).save(any());
    }
}
