package com.deepread.controller;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
import com.deepread.dto.response.ErrorResponse;
import com.deepread.dto.response.MeansResponseDto;
import com.deepread.service.ChatbotService;
import com.deepread.service.OpenAPIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Tag(name = "Chatbot", description = "단어 의미 조회 및 로그 저장 API")
@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final OpenAPIService openAPIService;

    @GetMapping("/meaning")
    @Operation(
            summary = "단어 의미만 조회",
            description = "외부 표준국어대사전 API를 통해 입력된 단어의 의미를 상세 항목(뜻, 문형, 예문 등)으로 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단어 의미 조회 성공",
                    content = @Content(schema = @Schema(implementation = MeansResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<MeansResponseDto> getMeaning(
            @Parameter(description = "의미를 조회할 단어", example = "사과", required = true)
            @RequestParam String word
    ) throws IOException {
        return ResponseEntity.ok(openAPIService.getMeans(word));
    }

    @Operation(
            summary = "단어 의미 조회 및 로그 저장",
            description = "입력된 단어의 의미를 조회하고 해당 요청을 사용자 ID와 함께 로그로 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공 및 로그 저장",
                    content = @Content(schema = @Schema(implementation = ChatbotLogResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "단어 의미 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/ask")
    public ResponseEntity<?> askAndSave(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @RequestParam Long userId,
            @Parameter(description = "의미를 조회할 단어", required = true, example = "사과")
            @RequestParam String word,
            HttpServletRequest request
    ) {
        try {
            MeansResponseDto meaningDto = openAPIService.getMeans(word);
            ChatbotLogRequestDto dto = new ChatbotLogRequestDto();
            dto.setUserId(userId);
            dto.setWord(word);
            dto.setResponse(meaningDto.getDefinition()); // DB에는 요약된 정의만 저장
            return ResponseEntity.ok(chatbotService.saveChatbotLog(dto));
        } catch (IOException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .error("Internal Server Error")
                    .message("단어 의미를 조회하는 중 오류가 발생했습니다.")
                    .status(500)
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(500).body(error);
        }
    }
}
