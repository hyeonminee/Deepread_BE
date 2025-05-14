package com.deepread.service;

import com.deepread.entity.ChatbotLog;
import com.deepread.repository.ChatbotLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatbotServiceTest {

    @Mock
    private ChatbotLogRepository chatbotLogRepository;

    @InjectMocks
    private ChatbotService chatbotService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveChatbotLog_success() {
        ChatbotLog log = new ChatbotLog();
        when(chatbotLogRepository.save(any())).thenReturn(log);

        ChatbotLog result = chatbotService.saveChatbotLog(log);

        assertNotNull(result);
        verify(chatbotLogRepository).save(log);
    }
}
