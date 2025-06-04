package com.deepread.repository;

import com.deepread.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    @Query(value = "SELECT * FROM quiz_questions WHERE level = :level ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<QuizQuestion> findRandom5ByLevel(String level);
}
