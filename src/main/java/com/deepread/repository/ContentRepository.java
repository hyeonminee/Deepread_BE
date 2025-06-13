package com.deepread.repository;

import com.deepread.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {
//    List<Content> findByLevel(Level level);

    Optional<Content> findByExternalIdAndCategory(Long externalId, String category);

    boolean existsByCategoryAndExternalId(String category, Long externalId);
}