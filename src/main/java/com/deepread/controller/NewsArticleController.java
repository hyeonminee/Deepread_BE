package com.deepread.controller;

import com.deepread.dto.response.NewsArticleResponseDto;
import com.deepread.entity.Content;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.ContentRepository;
import com.deepread.service.NewsArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "News", description = "뉴스 기사 관련 API")
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsArticleController {

    private final NewsArticleService newsArticleService;
    private final ContentRepository contentRepository;

    @Operation(summary = "뉴스 기사 AI 요약 수행", description = "지정한 뉴스 기사 ID의 원문을 AI로 요약하고 DB에 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 뉴스 없음"),
            @ApiResponse(responseCode = "500", description = "요약 처리 실패")
    })
    @PostMapping("/{id}/summarize")
    public ResponseEntity<NewsArticleResponseDto> summarizeArticleById(
            @Parameter(description = "뉴스 ID", example = "10")
            @PathVariable Long id
    ) throws Exception {
        NewsArticleResponseDto responseDto = newsArticleService.summarizeAndUpdate(id);
        return ResponseEntity.ok(responseDto);
    }


    @Operation(summary = "모든 뉴스 기사 조회", description = "DB에 저장된 모든 카테고리의 뉴스 기사 목록을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public List<NewsArticleResponseDto> getAllArticles() {
        return newsArticleService.getAllArticles();
    }

    @Operation(summary = "뉴스 기사 ID로 단건 조회", description = "뉴스 기사 ID를 기반으로 상세 원문 콘텐츠를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단건 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 뉴스 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsArticleResponseDto> getArticleById(
            @Parameter(description = "뉴스 ID", example = "12")
            @PathVariable Long id
    ) {
        return newsArticleService.getArticleById(id)
                .map(article -> {
                    NewsArticleResponseDto dto = new NewsArticleResponseDto();
                    dto.setId(article.getId());
                    dto.setCategory(article.getCategory());
                    dto.setTitle(article.getTitle());
                    dto.setContent(article.getContent());
                    dto.setAiSummary(article.getAiSummary());
                    dto.setOriginalUrl(article.getOriginalUrl());
                    dto.setCreatedAt(article.getCreatedAt());
                    dto.setContentId(contentRepository.findByExternalIdAndCategory(article.getId(), "NEWS")
                            .map(Content::getId)
                            .orElse(null));
                    return ResponseEntity.ok(dto);
                })
                .orElseThrow(() -> new ResourceNotFoundException("해당 뉴스 기사를 찾을 수 없습니다."));
    }

    @Operation(summary = "카테고리별 뉴스 기사 조회", description = "지정한 카테고리에 해당하는 뉴스 기사 목록을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 카테고리의 뉴스가 없음")
    })
    @GetMapping("/category/{category}")
    public List<NewsArticleResponseDto> getArticlesByCategory(@PathVariable String category) {
        return newsArticleService.getArticlesByCategory(category);
    }

    @Operation(summary = "제목 검색", description = "제목에 특정 키워드를 포함하는 뉴스 기사들을 검색하여 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공")
    })
    @GetMapping("/search")
    public List<NewsArticleResponseDto> searchByTitle(@RequestParam String keyword) {
        return newsArticleService.searchArticlesByTitle(keyword);
    }

    @Operation(summary = "오늘 뉴스 전체 조회", description = "오늘 날짜 기준으로 수집된 뉴스 기사 목록을 반환한다. (카드뉴스 UI 용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/today")
    public List<NewsArticleResponseDto> getTodayArticles() {
        return newsArticleService.getTodayArticles();
    }

    @Operation(summary = "오늘 뉴스 - 카테고리별 단건 조회", description = "오늘 날짜 + 지정 카테고리의 뉴스 기사 1건을 상세정보로 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 카테고리 또는 오늘 뉴스 없음")
    })
    @GetMapping("/today/category/{category}")
    public ResponseEntity<NewsArticleResponseDto> getTodayArticleByCategory(@PathVariable String category) {
        return newsArticleService.getSingleTodayArticleByCategory(category)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("오늘 날짜의 해당 카테고리 뉴스가 없습니다."));
    }

}
