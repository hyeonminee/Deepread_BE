package com.deepread.repository;

import com.deepread.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    List<UserReport> findByUserId(Long userId);
}
