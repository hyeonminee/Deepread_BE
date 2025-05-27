package com.deepread.controller;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
import com.deepread.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chatbot", description = "챗봇 대화 로그 관련 API")
@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(
            summary = "챗봇 대화 로그 저장",
            description = "사용자의 질문 및 챗봇 응답을 DB에 저장한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류 (유효성 검증 실패 등)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/log")
    public ResponseEntity<ChatbotLogResponseDto> saveChatLog(
            @Parameter(description = "사용자의 질문 및 챗봇 응답 내용이 포함된 DTO", required = true)
            @RequestBody @Valid ChatbotLogRequestDto dto) {
        ChatbotLogResponseDto responseDto = chatbotService.saveChatbotLog(dto);
        return ResponseEntity.ok(responseDto);
    }
}
