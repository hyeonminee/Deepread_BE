package com.deepread.repository;

import com.deepread.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Long> {
    List<ChatbotLog> findByUserId(Long userId);
}
