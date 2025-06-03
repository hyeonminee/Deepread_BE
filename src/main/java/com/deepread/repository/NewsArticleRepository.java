package com.deepread.repository;

import com.deepread.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {

    Optional<NewsArticle> findTopByCategoryOrderByCreatedAtDesc(String category);

    List<NewsArticle> findByCategory(String category);

    List<NewsArticle> findByTitleContaining(String keyword);

    List<NewsArticle> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<NewsArticle> findByCategoryAndCreatedAtBetween(String category, LocalDateTime start, LocalDateTime end);

}
