package com.deepread.controller;

import com.deepread.entity.User;
import com.deepread.entity.Content;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Content", description = "문해력 콘텐츠 관련 API")
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

//    @Operation(
//            summary = "추천 콘텐츠 조회",
//            description = "사용자의 문해력 레벨(초급, 중급, 고급)에 따라 적절한 콘텐츠를 추천한다."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "추천 콘텐츠 반환 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 레벨 입력"),
//            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
//    })
//    @GetMapping("/recommend")
//    public List<Content> getRecommendedContents(
//            @Parameter(
//                    name = "level",
//                    description = "사용자의 문해력 수준 (초급, 중급, 고급 중 하나)",
//                    example = "초급"
//            )
//            @RequestParam String level) {
//        return contentService.getRecommendedContents(User.Level.valueOf(level));
//    }

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
}
