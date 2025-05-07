package com.deepread.service;

import com.deepread.entity.ChatbotLog;
import com.deepread.repository.ChatbotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ChatbotLogRepository chatbotLogRepository;

    // 사용자 질문과 챗봇 응답을 로그로 저장
    public ChatbotLog saveChatbotLog(ChatbotLog chatbotLog) {
        return chatbotLogRepository.save(chatbotLog);
    }
}
