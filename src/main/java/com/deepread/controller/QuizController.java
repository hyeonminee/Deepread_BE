package com.deepread.controller;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(responseCode = "200", description = "퀴즈 결과 저장 완료")
    @PostMapping("/result")
    public ResponseEntity<QuizResultResponseDto> saveQuizResult(@RequestBody @Valid QuizResultRequestDto dto) {
        QuizResultResponseDto responseDto = quizService.saveQuizResult(dto);
        return ResponseEntity.ok(responseDto);
    }
}
