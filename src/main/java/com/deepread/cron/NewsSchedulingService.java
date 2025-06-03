package com.deepread.cron;

import com.deepread.service.NewsCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsSchedulingService {

    private final NewsCrawlerService newsCrawlerService;

    @Scheduled(cron = "0 30 7 * * *", zone = "Asia/Seoul")  // 매일 오전 7시 30분
    public void runNewsCrawling() {
        newsCrawlerService.crawlAndSaveArticles();
    }
}
