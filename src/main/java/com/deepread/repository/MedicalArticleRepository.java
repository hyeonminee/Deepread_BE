package com.deepread.repository;

import com.deepread.entity.MedicalArticle;
import com.deepread.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalArticleRepository extends JpaRepository<MedicalArticle, Long> {

    List<MedicalArticle> findByLevel(User.Level level);
}
