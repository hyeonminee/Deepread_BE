package com.deepread.controller;

import com.deepread.dto.response.RecommendedArticlesDto;
import com.deepread.entity.Content;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.ArticleRecommendationService;
import com.deepread.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Content", description = "문해력 콘텐츠 관련 API")
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final ArticleRecommendationService recommendationService;

    @Operation(
            summary = "콘텐츠 상세 조회",
            description = "콘텐츠 ID를 통해 상세 콘텐츠 정보를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "콘텐츠 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 콘텐츠를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public Content getContentById(
            @Parameter(name = "id", description = "조회할 콘텐츠의 고유 ID", example = "1")
            @PathVariable Long id) {
        return contentService.getContentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("콘텐츠를 찾을 수 없습니다."));
    }

    @Operation(
            summary = "레벨 기반 추천 콘텐츠 조회 (로그인 사용자)",
            description = "현재 로그인한 사용자의 문해력 수준에 맞춰 법률, 의료, 뉴스 콘텐츠를 추천합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 콘텐츠 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/recommend")
    public RecommendedArticlesDto getRecommendedContents(@AuthenticationPrincipal User user) {
        return recommendationService.getRecommendedArticles(user.getId());
    }
}
