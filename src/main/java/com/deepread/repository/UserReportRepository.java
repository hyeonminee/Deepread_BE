package com.deepread.repository;

import com.deepread.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    Optional<UserReport> findByUserId(Long userId);
}
