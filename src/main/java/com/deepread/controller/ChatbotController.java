package com.deepread.controller;

import com.deepread.entity.ChatbotLog;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "챗봇 대화 로그 저장", description = "사용자의 질문과 챗봇의 응답을 저장한다.")
    @ApiResponse(responseCode = "200", description = "로그 저장 성공")
    @PostMapping("/log")
    public ResponseEntity<?> saveChatLog(@RequestBody ChatbotLog log) {
        if (log == null || log.getUser() == null || log.getQuestion() == null) {
            throw new ResourceNotFoundException("챗봇 로그 또는 사용자 정보가 유효하지 않습니다.");
        }

        ChatbotLog saved = chatbotService.saveChatbotLog(log);
        return ResponseEntity.ok(saved);
    }
}
