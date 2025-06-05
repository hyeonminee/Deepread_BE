package com.deepread.repository;

import com.deepread.entity.SummaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummaryFeedbackRepository extends JpaRepository<SummaryFeedback, Long> {
    Optional<SummaryFeedback> findBySummaryId(Long summaryId);
}
