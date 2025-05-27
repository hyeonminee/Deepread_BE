package com.deepread.repository;

import com.deepread.entity.DiagnosisQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiagnosisQuestionRepository extends JpaRepository<DiagnosisQuestion, Long> {

    @Query(value = "SELECT * FROM diagnosis_questions WHERE type = :type ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<DiagnosisQuestion> findRandomByType(@Param("type") String type, @Param("count") int count);

    List<DiagnosisQuestion> findByType(String type); // 전체 불러오기 (필요 시)
}
