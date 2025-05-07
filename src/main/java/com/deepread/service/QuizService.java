package com.deepread.service;

import com.deepread.entity.QuizResult;
import com.deepread.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizResultRepository quizResultRepository;

    // 퀴즈 결과 저장
    public QuizResult saveQuizResult(QuizResult quizResult) {
        return quizResultRepository.save(quizResult);
    }

    // (선택 기능) 사용자별 퀴즈 결과 조회
    public List<QuizResult> getQuizResultsByUserId(Long userId) {
        return quizResultRepository.findByUserId(userId);
    }
}
