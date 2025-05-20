package com.deepread.service;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.entity.QuizResult;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.QuizResultRepository;
import com.deepread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    private QuizService quizService;
    private QuizResultRepository quizResultRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        quizResultRepository = mock(QuizResultRepository.class);
        userRepository = mock(UserRepository.class);
        modelMapper = new ModelMapper();
        quizService = new QuizService(quizResultRepository, userRepository, modelMapper);
    }

    @Test
    @DisplayName("퀴즈 결과 저장 성공")
    void saveQuizResult_success() {
        // given
        Long userId = 1L;
        QuizResultRequestDto requestDto = new QuizResultRequestDto();
        requestDto.setUserId(userId);
        requestDto.setCorrectCount(7);
        requestDto.setTotalQuestions(10);
        requestDto.setAccuracy(70.0f);

        User user = new User();
        user.setId(userId);

        QuizResult savedResult = new QuizResult();
        savedResult.setId(100L);
        savedResult.setUser(user);
        savedResult.setCorrectCount(7);
        savedResult.setTotalQuestions(10);
        savedResult.setAccuracy(70.0f);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(quizResultRepository.save(any(QuizResult.class))).thenReturn(savedResult);

        // when
        QuizResultResponseDto responseDto = quizService.saveQuizResult(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(100L, responseDto.getId());
        assertEquals(userId, responseDto.getUserId());
        assertEquals(7, responseDto.getCorrectCount());
        assertEquals(10, responseDto.getTotalQuestions());
        assertEquals(70.0f, responseDto.getAccuracy());

        verify(userRepository).findById(userId);
        verify(quizResultRepository).save(any(QuizResult.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 예외 발생")
    void saveQuizResult_userNotFound() {
        // given
        Long invalidUserId = 999L;
        QuizResultRequestDto requestDto = new QuizResultRequestDto();
        requestDto.setUserId(invalidUserId);

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> {
            quizService.saveQuizResult(requestDto);
        });

        verify(userRepository).findById(invalidUserId);
        verify(quizResultRepository, never()).save(any(QuizResult.class));
    }
}
