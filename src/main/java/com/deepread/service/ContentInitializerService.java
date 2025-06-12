package com.deepread.service;

import com.deepread.entity.Content;
import com.deepread.entity.LawArticle;
import com.deepread.entity.MedicalArticle;
import com.deepread.entity.NewsArticle;
import com.deepread.repository.ContentRepository;
import com.deepread.repository.LawArticleRepository;
import com.deepread.repository.MedicalArticleRepository;
import com.deepread.repository.NewsArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentInitializerService {

    private final LawArticleRepository lawArticleRepository;
    private final MedicalArticleRepository medicalArticleRepository;
    private final NewsArticleRepository newsArticleRepository;
    private final ContentRepository contentRepository;

    public void initializeContents() {
        log.info("=== Content 테이블 초기화 시작 ===");

        // 1. 법률 콘텐츠 등록
        List<LawArticle> laws = lawArticleRepository.findAll();
        for (LawArticle law : laws) {
            boolean exists = contentRepository.existsByCategoryAndExternalId("LAW", law.getId());
            if (!exists) {
                contentRepository.save(Content.builder()
                        .category("LAW")
                        .externalId(law.getId())
                        .title(law.getTheme())
                        .summarySourceType("CSV")
                        .build());
            }
        }

        // 2. 의료 콘텐츠 등록
        List<MedicalArticle> medicals = medicalArticleRepository.findAll();
        for (MedicalArticle medical : medicals) {
            boolean exists = contentRepository.existsByCategoryAndExternalId("MEDICAL", medical.getId());
            if (!exists) {
                contentRepository.save(Content.builder()
                        .category("MEDICAL")
                        .externalId(medical.getId())
                        .title(medical.getTheme())
                        .summarySourceType("CSV")
                        .build());
            }
        }

        // 3. 뉴스 콘텐츠 등록
        List<NewsArticle> newsList = newsArticleRepository.findAll();
        for (NewsArticle news : newsList) {
            boolean exists = contentRepository.existsByCategoryAndExternalId("NEWS", news.getId());
            if (!exists) {
                contentRepository.save(Content.builder()
                        .category("NEWS")
                        .externalId(news.getId())
                        .title(news.getTitle())
                        .summarySourceType("CRAWL")
                        .build());
            }
        }

        log.info("=== Content 테이블 초기화 완료 ===");
    }
}
