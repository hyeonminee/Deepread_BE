package com.deepread.controller;

import com.deepread.entity.User;
import com.deepread.entity.Content;
import com.deepread.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // 사용자의 레벨에 따라 콘텐츠 추천
    @GetMapping("/recommend") // @GetMapping("/recommend"): 사용자의 레벨에 따라 콘텐츠를 추천
    public List<Content> getRecommendedContents(@RequestParam String level) {
        return contentService.getRecommendedContents(User.Level.valueOf(level));
    }

    // 콘텐츠 ID로 콘텐츠 상세 조회
    @GetMapping("/{id}") // @GetMapping("/{id}"): 특정 ID의 콘텐츠 내용을 조회
    public Content getContentById(@PathVariable Long id) { // @PathVariable: URL 경로에 포함된 변수를 추출해 메서드 파라미터로 전달
        return contentService.getContentById(id)
                .orElseThrow(() -> new RuntimeException("콘텐츠를 찾을 수 없습니다."));
    }
}
