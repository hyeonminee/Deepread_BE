package com.deepread.repository;

import com.deepread.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Long> {

    @Query("SELECT COUNT(DISTINCT c.word) FROM ChatbotLog c WHERE c.user.id = :userId")
    int countDistinctWordsByUserId(@Param("userId") Long userId);
}
