package com.deepread.service;

import com.deepread.entity.NewsArticle;
import com.deepread.repository.NewsArticleRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsCrawlerService {

    private static final Logger log = LoggerFactory.getLogger(NewsCrawlerService.class);

    private final NewsArticleRepository newsArticleRepository;

    private final Map<String, String> categoryMap = Map.of(
            "경제", "https://www.yna.co.kr/economy/index?site=navi_economy_depth01",
            "사회", "https://www.yna.co.kr/society/index?site=navi_society_depth01",
            "문화", "https://www.yna.co.kr/culture/index?site=navi_culture_depth01",
            "스포츠", "https://www.yna.co.kr/sports/index?site=navi_sports_depth01",
            "세계", "https://www.yna.co.kr/international/index?site=navi_international_depth01",
            "산업", "https://www.yna.co.kr/industry/index?site=navi_industry_depth01",
            "연예", "https://www.yna.co.kr/entertainment/index?site=navi_entertainment_depth01"
    );

    public void crawlAndSaveArticles() {
        log.info("===== 뉴스 크롤링 시작 =====");
        int savedCount = 0;

        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            String category = entry.getKey();
            String categoryUrl = entry.getValue();

            try {
                Optional<NewsArticle> existing = newsArticleRepository.findTopByCategoryOrderByCreatedAtDesc(category);
                if (existing.isPresent() && existing.get().getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
                    log.info("[SKIP] 오늘 이미 저장된 뉴스 존재 - 카테고리: {}", category);
                    continue;
                }

                // Jsoup 요청
                Document categoryDoc = Jsoup.connect(categoryUrl)
                        .userAgent("Mozilla/5.0 (compatible; DeepReadBot/1.0)")
                        .get();

                Element linkElement = categoryDoc.selectFirst("strong.tit-wrap > a");
                if (linkElement == null) {
                    log.warn("[경고] 링크 추출 실패 - 카테고리: {}", category);
                    continue;
                }

                String newsUrl = linkElement.absUrl("href");

                Document detailDoc = Jsoup.connect(newsUrl)
                        .userAgent("Mozilla/5.0 (compatible; DeepReadBot/1.0)")
                        .get();

                String title = detailDoc.selectFirst("h1.tit01").text();
                String content = detailDoc.select("div.story-news p").eachText().stream()
                        .reduce("", (acc, p) -> acc + "\n" + p);

                // AI 요약 없이 저장 (aiSummary = null)
                NewsArticle article = new NewsArticle();
                article.setCategory(category);
                article.setTitle(title);
                article.setContent(content);
                article.setAiSummary(null); // 요약 없이 저장. 요약은 나중에 진행
                article.setOriginalUrl(newsUrl);

                newsArticleRepository.save(article);
                savedCount++;
                log.info("[저장 완료] 카테고리: {}, 제목: {}", category, title);

            } catch (Exception e) {
                log.error("[크롤링 실패] 카테고리: {}, 오류: {}", category, e.getMessage(), e);
            }
        }

        log.info("===== 뉴스 크롤링 완료: 저장된 기사 수 = {} =====", savedCount);
    }
}
