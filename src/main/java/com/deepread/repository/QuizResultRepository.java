package com.deepread.repository;

import com.deepread.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findByUserId(Long userId);

    @Query("SELECT COALESCE(AVG(qr.accuracy), 0.0) FROM QuizResult qr WHERE qr.user.id = :userId AND qr.submittedAt BETWEEN :start AND :end")
    Double findAverageScoreByUserIdAndPeriod(@Param("userId") Long userId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
}
