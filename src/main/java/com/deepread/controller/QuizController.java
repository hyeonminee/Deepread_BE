package com.deepread.controller;

import com.deepread.entity.QuizResult;
import com.deepread.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // 사용자의 퀴즈 결과 저장
    @PostMapping("/result")
    public QuizResult saveQuizResult(@RequestBody QuizResult quizResult) {
        return quizService.saveQuizResult(quizResult);
    }
}
