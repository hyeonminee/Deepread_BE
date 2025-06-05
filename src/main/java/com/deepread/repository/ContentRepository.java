package com.deepread.repository;

import com.deepread.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
//    List<Content> findByLevel(Level level);
}