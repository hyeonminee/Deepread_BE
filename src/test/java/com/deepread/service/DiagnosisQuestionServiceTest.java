package com.deepread.service;

import com.deepread.dto.response.DiagnosisQuestionResponseDto;
import com.deepread.entity.DiagnosisQuestion;
import com.deepread.repository.DiagnosisQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiagnosisQuestionServiceTest {

    private DiagnosisQuestionRepository questionRepository;
    private DiagnosisQuestionService diagnosisQuestionService;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        questionRepository = mock(DiagnosisQuestionRepository.class);
        modelMapper = new ModelMapper();
        diagnosisQuestionService = new DiagnosisQuestionService(questionRepository, modelMapper);
    }

    @Test
    @DisplayName("타입별 진단 문항 무작위 추출 성공")
    void getMixedQuestions_success() {
        // given: 각 타입별 충분한 문항
        DiagnosisQuestion q1 = new DiagnosisQuestion(); q1.setId(1L); q1.setType("A"); q1.setQuestion("A1?");
        DiagnosisQuestion q2 = new DiagnosisQuestion(); q2.setId(2L); q2.setType("B"); q2.setQuestion("B1?");
        DiagnosisQuestion q3 = new DiagnosisQuestion(); q3.setId(3L); q3.setType("B"); q3.setQuestion("B2?");
        DiagnosisQuestion q4 = new DiagnosisQuestion(); q4.setId(4L); q4.setType("C"); q4.setQuestion("C1?");
        DiagnosisQuestion q5 = new DiagnosisQuestion(); q5.setId(5L); q5.setType("C"); q5.setQuestion("C2?");
        DiagnosisQuestion q6 = new DiagnosisQuestion(); q6.setId(6L); q6.setType("A"); q6.setQuestion("A2?");
        DiagnosisQuestion q7 = new DiagnosisQuestion(); q7.setId(7L); q7.setType("B"); q7.setQuestion("B3?");
        DiagnosisQuestion q8 = new DiagnosisQuestion(); q8.setId(8L); q8.setType("C"); q8.setQuestion("C3?");

        when(questionRepository.findByType("A")).thenReturn(Arrays.asList(q1, q6));
        when(questionRepository.findByType("B")).thenReturn(Arrays.asList(q2, q3, q7));
        when(questionRepository.findByType("C")).thenReturn(Arrays.asList(q4, q5, q8));

        // when
        List<DiagnosisQuestionResponseDto> result = diagnosisQuestionService.getMixedQuestions();

        // then
        assertEquals(5, result.size()); // A 1개, B 2개, C 2개
        assertTrue(result.stream().anyMatch(dto -> dto.getQuestion().contains("A")));
        assertTrue(result.stream().anyMatch(dto -> dto.getQuestion().contains("B")));
        assertTrue(result.stream().anyMatch(dto -> dto.getQuestion().contains("C")));

        verify(questionRepository).findByType("A");
        verify(questionRepository).findByType("B");
        verify(questionRepository).findByType("C");
    }
}
