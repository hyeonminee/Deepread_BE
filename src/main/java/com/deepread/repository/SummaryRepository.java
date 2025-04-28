package com.deepread.repository;

import com.deepread.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    List<Summary> findByUserId(Long userId);
}
