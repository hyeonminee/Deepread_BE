package com.deepread.repository;

import com.deepread.entity.MedicalArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalArticleRepository extends JpaRepository<MedicalArticle, Long> {

    // 향후 title 기반 검색 등 추가 가능
}
