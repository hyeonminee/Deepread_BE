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
        dto.setWord("사과");
        dto.setResponse("사과는 과일입니다.");

        User user = new User();
        user.setId(userId);

        ChatbotLog savedLog = new ChatbotLog();
        savedLog.setId(10L);
        savedLog.setUser(user);
        savedLog.setWord("사과");
        savedLog.setResponse("사과는 과일입니다.");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatbotLogRepository.existsByUser_IdAndWord(userId, "사과")).thenReturn(false);
        when(chatbotLogRepository.save(any(ChatbotLog.class))).thenReturn(savedLog);

        // when
        ChatbotLogResponseDto response = chatbotService.saveChatbotLog(dto);

        // then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("사과", response.getWord());
        assertEquals("사과는 과일입니다.", response.getResponse());
        assertEquals(userId, response.getUserId());
        verify(chatbotLogRepository).save(any(ChatbotLog.class));
    }

    @Test
    @DisplayName("챗봇 로그 저장 실패 - 사용자 없음")
    void saveChatbotLog_userNotFound() {
        // given
        ChatbotLogRequestDto dto = new ChatbotLogRequestDto();
        dto.setUserId(999L);
        dto.setWord("바나나");
        dto.setResponse("응답");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> chatbotService.saveChatbotLog(dto));
        verify(chatbotLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("챗봇 로그 저장 실패 - 중복된 단어")
    void saveChatbotLog_alreadyExists() {
        // given
        Long userId = 1L;
        ChatbotLogRequestDto dto = new ChatbotLogRequestDto();
        dto.setUserId(userId);
        dto.setWord("포도");
        dto.setResponse("포도는 보라색입니다.");

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatbotLogRepository.existsByUser_IdAndWord(userId, "포도")).thenReturn(true);

        // when
        ChatbotLogResponseDto response = chatbotService.saveChatbotLog(dto);

        // then
        assertNull(response); // 이미 존재하므로 저장하지 않음
        verify(chatbotLogRepository, never()).save(any());
    }
}
