package com.deepread.repository;

import com.deepread.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SummaryRepository extends JpaRepository<Summary, Long> {

    List<Summary> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Summary> findByUserId(Long userId);

    // 평균 점수 계산 쿼리 추가
    @Query("SELECT AVG(s.score) FROM Summary s WHERE s.user.id = :userId AND s.createdAt BETWEEN :start AND :end AND s.score IS NOT NULL")
    Double findAverageScoreByUserIdAndPeriod(@Param("userId") Long userId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
}
