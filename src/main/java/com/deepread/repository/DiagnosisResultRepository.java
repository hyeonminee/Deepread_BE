package com.deepread.repository;

import com.deepread.entity.DiagnosisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiagnosisResultRepository extends JpaRepository<DiagnosisResult, Long> {
    List<DiagnosisResult> findByUserId(Long userId);
}