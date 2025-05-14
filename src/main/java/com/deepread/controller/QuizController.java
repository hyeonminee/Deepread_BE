package com.deepread.controller;

import com.deepread.entity.QuizResult;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    public ResponseEntity<?> saveQuizResult(@RequestBody QuizResult quizResult) {
        if (quizResult == null || quizResult.getUser() == null) {
            throw new ResourceNotFoundException("퀴즈 결과 또는 사용자 정보가 유효하지 않습니다.");
        }

        QuizResult saved = quizService.saveQuizResult(quizResult);
        return ResponseEntity.ok(saved);
    }
}
