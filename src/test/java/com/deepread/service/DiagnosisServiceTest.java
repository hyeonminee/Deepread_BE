package com.deepread.service;

import com.deepread.entity.DiagnosisResult;
import com.deepread.repository.DiagnosisResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiagnosisServiceTest {

    @Mock
    private DiagnosisResultRepository diagnosisResultRepository;

    @InjectMocks
    private DiagnosisService diagnosisService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitDiagnosisResult_success() {
        DiagnosisResult result = new DiagnosisResult();
        when(diagnosisResultRepository.save(any())).thenReturn(result);

        DiagnosisResult saved = diagnosisService.submitDiagnosisResult(result);

        assertNotNull(saved);
        verify(diagnosisResultRepository).save(result);
    }
}
