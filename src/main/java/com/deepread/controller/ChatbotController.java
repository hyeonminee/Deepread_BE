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
    @Operation(summary = "단어 전체 의미 조회 (관리자)", description = "단어의 뜻, 품사, 한자, 예문, 유의어, 반의어를 모두 포함한 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단어 전체 의미 조회 성공",
                    content = @Content(schema = @Schema(implementation = MeansResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getMeaning(@RequestParam String word, HttpServletRequest request) {
        try {
            MeansResponseDto responseDto = openAPIService.getMeans(word);
            return ResponseEntity.ok(responseDto);
        } catch (IOException e) {
            return buildError("단어 의미를 조회하는 중 오류가 발생했습니다.", request);
        }
    }

    @PostMapping("/ask")
    @Operation(summary = "단어 의미 조회 및 로그 저장 (실제 사용)", description = "입력된 단어의 의미를 조회하고 로그로 저장합니다. 중복 조회는 저장되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공 및 로그 저장",
                    content = @Content(schema = @Schema(implementation = ChatbotLogResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "이미 조회한 단어입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "의미 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> askAndSave(
            @Parameter(description = "사용자 ID", required = true, example = "1") @RequestParam Long userId,
            @Parameter(description = "의미를 조회할 단어", required = true, example = "사과") @RequestParam String word,
            HttpServletRequest request
    ) {
        try {
            MeansResponseDto meaningDto = openAPIService.getMeans(word);

            ChatbotLogRequestDto dto = new ChatbotLogRequestDto();
            dto.setUserId(userId);
            dto.setWord(word);
            dto.setResponse(meaningDto.getDefinition());

            ChatbotLogResponseDto saved = chatbotService.saveChatbotLog(dto);

            if (saved == null) {
                return ResponseEntity.status(409).body(ErrorResponse.builder()
                        .error("Conflict")
                        .message("이미 조회한 단어입니다.")
                        .status(409)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());
            }

            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return buildError("단어 의미를 조회하는 중 오류가 발생했습니다.", request);
        }
    }

    @GetMapping("/hanja")
    @Operation(summary = "한자 뜻풀이 반환", description = "단어의 한자어 표기를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "한자 반환 성공"),
            @ApiResponse(responseCode = "500", description = "한자 반환 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getHanja(@RequestParam String word, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(openAPIService.getMeans(word).getHanja());
        } catch (IOException e) {
            return buildError("한자 정보를 가져오는 중 오류가 발생했습니다.", request);
        }
    }

    @GetMapping("/example")
    @Operation(summary = "예문 반환", description = "단어에 대한 예문을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예문 반환 성공"),
            @ApiResponse(responseCode = "500", description = "예문 반환 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getExample(@RequestParam String word, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(openAPIService.getMeans(word).getExample());
        } catch (IOException e) {
            return buildError("예문을 가져오는 중 오류가 발생했습니다.", request);
        }
    }

    @GetMapping("/synonym")
    @Operation(summary = "유의어 반환", description = "단어의 유의어를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유의어 반환 성공"),
            @ApiResponse(responseCode = "500", description = "유의어 반환 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getSynonym(@RequestParam String word, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(openAPIService.getMeans(word).getSynonym());
        } catch (IOException e) {
            return buildError("유의어를 가져오는 중 오류가 발생했습니다.", request);
        }
    }

    @GetMapping("/antonym")
    @Operation(summary = "반의어 반환", description = "단어의 반의어를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반의어 반환 성공"),
            @ApiResponse(responseCode = "500", description = "반의어 반환 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getAntonym(@RequestParam String word, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(openAPIService.getMeans(word).getAntonym());
        } catch (IOException e) {
            return buildError("반의어를 가져오는 중 오류가 발생했습니다.", request);
        }
    }

    private ResponseEntity<ErrorResponse> buildError(String message, HttpServletRequest request) {
        return ResponseEntity.status(500).body(ErrorResponse.builder()
                .error("Internal Server Error")
                .message(message)
                .status(500)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
