package com.deepread.controller;

import com.deepread.service.NewsCrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "News Test", description = "뉴스 크롤링 테스트 API")
@RestController
@RequestMapping("/api/news/test")
@RequiredArgsConstructor
public class NewsCrawlTestController {

    private final NewsCrawlerService newsCrawlerService;

    @Operation(summary = "뉴스 수동 크롤링 실행", description = "모든 카테고리 뉴스에 대해 크롤링을 수동으로 수행하고 저장한다.")
    @PostMapping("/crawl")
    public ResponseEntity<String> manualCrawl() {
        newsCrawlerService.crawlAndSaveArticles();
        return ResponseEntity.ok("크롤링 수동 실행 완료");
    }
}
