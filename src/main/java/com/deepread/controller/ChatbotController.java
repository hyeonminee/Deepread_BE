package com.deepread.controller;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
import com.deepread.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "챗봇 대화 로그 저장", description = "사용자의 질문과 챗봇의 응답을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류 (유효성 검증 실패 등)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/log")
    public ResponseEntity<ChatbotLogResponseDto> saveChatLog(@RequestBody @Valid ChatbotLogRequestDto dto) {
        ChatbotLogResponseDto responseDto = chatbotService.saveChatbotLog(dto);
        return ResponseEntity.ok(responseDto);
    }
}
