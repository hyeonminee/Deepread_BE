package com.deepread.service;

import com.deepread.entity.DiagnosisResult;
import com.deepread.repository.DiagnosisResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisResultRepository diagnosisResultRepository;

    // 진단 결과 제출
    public DiagnosisResult submitDiagnosisResult(DiagnosisResult result) {
        return diagnosisResultRepository.save(result);
    }
}
