package com.deepread.service;

import com.deepread.entity.QuizResult;
import com.deepread.repository.QuizResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    @Mock
    private QuizResultRepository quizResultRepository;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveQuizResult_success() {
        QuizResult quiz = new QuizResult();
        when(quizResultRepository.save(any())).thenReturn(quiz);

        QuizResult result = quizService.saveQuizResult(quiz);

        assertNotNull(result);
        verify(quizResultRepository).save(quiz);
    }
}
