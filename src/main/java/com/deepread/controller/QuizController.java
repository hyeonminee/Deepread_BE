package com.deepread.controller;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 결과 저장", description = "사용자의 퀴즈 풀이 결과를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀴즈 결과 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (유효성 검증 실패 등)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/result")
    public ResponseEntity<QuizResultResponseDto> saveQuizResult(@RequestBody @Valid QuizResultRequestDto dto) {
        QuizResultResponseDto responseDto = quizService.saveQuizResult(dto);
        return ResponseEntity.ok(responseDto);
    }
}
