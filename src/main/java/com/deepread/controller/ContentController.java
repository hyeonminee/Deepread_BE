package com.deepread.controller;

import com.deepread.entity.User;
import com.deepread.entity.Content;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @Operation(summary = "추천 콘텐츠 조회", description = "사용자의 문해력 레벨에 따라 콘텐츠를 추천한다.")
    @ApiResponse(responseCode = "200", description = "추천 콘텐츠 반환")
    @GetMapping("/recommend") // @GetMapping("/recommend"): 사용자의 레벨에 따라 콘텐츠를 추천
    public List<Content> getRecommendedContents(@RequestParam String level) {
        return contentService.getRecommendedContents(User.Level.valueOf(level));
    }

    @Operation(summary = "콘텐츠 상세 조회", description = "콘텐츠 ID를 통해 상세 내용을 조회한다.")
    @ApiResponse(responseCode = "200", description = "콘텐츠 반환")
    @GetMapping("/{id}") // @GetMapping("/{id}"): 특정 ID의 콘텐츠 내용을 조회
    public Content getContentById(@PathVariable Long id) { // @PathVariable: URL 경로에 포함된 변수를 추출해 메서드 파라미터로 전달
        return contentService.getContentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("콘텐츠를 찾을 수 없습니다."));
    }
}
