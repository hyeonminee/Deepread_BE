package com.deepread.controller;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
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
    public ResponseEntity<ChatbotLogResponseDto> saveChatLog(@RequestBody ChatbotLogRequestDto dto) {
        ChatbotLogResponseDto responseDto = chatbotService.saveChatbotLog(dto);
        return ResponseEntity.ok(responseDto);
    }
}
