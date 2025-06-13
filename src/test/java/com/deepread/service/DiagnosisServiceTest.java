package com.deepread.service;

import com.deepread.config.ModelMapperConfig;
import com.deepread.dto.request.DiagnosisResultRequestDto;
import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.entity.DiagnosisResult;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.DiagnosisResultRepository;
import com.deepread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiagnosisServiceTest {

    private DiagnosisService diagnosisService;
    private DiagnosisResultRepository diagnosisResultRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        diagnosisResultRepository = mock(DiagnosisResultRepository.class);
        userRepository = mock(UserRepository.class);
        modelMapper = new ModelMapperConfig().modelMapper();
        diagnosisService = new DiagnosisService(diagnosisResultRepository, userRepository, modelMapper);
    }

    @Test
    @DisplayName("진단 결과 저장 성공")
    void submitDiagnosisResult_success() {
        // given
        Long userId = 1L;
        DiagnosisResultRequestDto requestDto = new DiagnosisResultRequestDto();
        requestDto.setUserId(userId);
        requestDto.setScore(85);
        requestDto.setUserLevel(User.Level.중급);

        User user = new User();
        user.setId(userId);

        DiagnosisResult savedResult = new DiagnosisResult();
        savedResult.setId(10L);
        savedResult.setUser(user);
        savedResult.setScore(85);
        savedResult.setUserLevel(User.Level.중급);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(diagnosisResultRepository.save(any(DiagnosisResult.class))).thenReturn(savedResult);

        // when
        DiagnosisResultResponseDto responseDto = diagnosisService.submitDiagnosisResult(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(userId, responseDto.getUserId());
        assertEquals(85, responseDto.getScore());
        assertEquals(User.Level.중급, responseDto.getUserLevel());

        verify(userRepository).findById(userId);
        verify(diagnosisResultRepository).save(any(DiagnosisResult.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 예외 발생")
    void submitDiagnosisResult_userNotFound() {
        // given
        Long invalidUserId = 999L;
        DiagnosisResultRequestDto requestDto = new DiagnosisResultRequestDto();
        requestDto.setUserId(invalidUserId);
        requestDto.setScore(50);
        requestDto.setUserLevel(User.Level.초급);

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () ->
                diagnosisService.submitDiagnosisResult(requestDto));

        verify(userRepository).findById(invalidUserId);
        verify(diagnosisResultRepository, never()).save(any());
    }
}
