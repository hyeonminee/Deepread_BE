package com.deepread.controller;

import com.deepread.entity.ChatbotLog;
import com.deepread.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    // 사용자 질문과 챗봇 응답 저장
    @PostMapping("/log")
    public ChatbotLog saveChatLog(@RequestBody ChatbotLog log) {
        return chatbotService.saveChatbotLog(log);
    }
}
