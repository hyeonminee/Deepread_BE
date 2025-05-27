package com.deepread.controller;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Quiz", description = "퀴즈 관련 API")
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(
            summary = "퀴즈 결과 저장",
            description = "사용자가 푼 퀴즈 결과(정답 개수, 총 문항 수 등)를 저장하고 결과를 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀴즈 결과 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류 (예: 필수 필드 누락)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/result")
    public ResponseEntity<QuizResultResponseDto> saveQuizResult(@RequestBody @Valid QuizResultRequestDto dto) {
        QuizResultResponseDto responseDto = quizService.saveQuizResult(dto);
        return ResponseEntity.ok(responseDto);
    }
}