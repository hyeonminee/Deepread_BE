package com.deepread.repository;

import com.deepread.entity.LawArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LawArticleRepository extends JpaRepository<LawArticle, Long> {

    // 향후 카테고리(주제)별 조회용
    List<LawArticle> findByTheme(String theme);

    // 제목 혹은 내용 검색용 (선택사항)
    List<LawArticle> findByContentContaining(String keyword);
}
