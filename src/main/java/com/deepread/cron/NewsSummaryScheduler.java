package com.deepread.cron;

import com.deepread.entity.NewsArticle;
import com.deepread.repository.NewsArticleRepository;
import com.deepread.client.AiSummaryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsSummaryScheduler {

    private final NewsArticleRepository newsArticleRepository;
    private final AiSummaryClient aiSummaryClient;

    @Scheduled(cron = "0 35 17 * * *", zone = "Asia/Seoul") // 매일 오전 8시
    @Transactional
    public void summarizeNewsWithoutAiSummary() {
        log.info("[AI 요약 스케줄러 시작] aiSummary=null인 뉴스 기사 요약 시작");

        List<NewsArticle> unsummarizedArticles = newsArticleRepository.findAll().stream()
                .filter(article -> article.getAiSummary() == null)
                .toList();

        int successCount = 0;

        for (NewsArticle article : unsummarizedArticles) {
            try {
                String summary = aiSummaryClient.requestSummary(article.getContent());
                article.setAiSummary(summary);
                successCount++;
            } catch (Exception e) {
                log.warn("요약 실패: 기사 ID={}, title={}, 이유={}", article.getId(), article.getTitle(), e.getMessage());
            }
        }

        log.info("[AI 요약 스케줄러 종료] 요약 완료: {}건", successCount);
    }
}
