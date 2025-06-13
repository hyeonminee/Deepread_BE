package com.deepread.controller;

import com.deepread.dto.response.LawArticleResponseDto;
import com.deepread.dto.response.LawArticleUploadResponseDto;
import com.deepread.entity.Content;
import com.deepread.entity.User;
import com.deepread.oauth.CustomPrincipal;
import com.deepread.repository.ContentRepository;
import com.deepread.service.LawArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Law", description = "법률 콘텐츠 관련 API")
@RestController
@RequestMapping("/api/law")
@RequiredArgsConstructor
public class LawArticleController {

    private final LawArticleService lawArticleService;
    private final ContentRepository contentRepository;

    @Operation(summary = "법률 콘텐츠 조회", description = "로그인한 사용자의 레벨에 해당하는 콘텐츠만 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping
    public List<LawArticleResponseDto> getByUserLevel(@AuthenticationPrincipal CustomPrincipal principal) {
        User.Level level = principal.getUser().getLevel(); // 또는 principal.getLevel()
        return lawArticleService.getArticlesByLevel(level);
    }

    @GetMapping("/level")
    public List<LawArticleResponseDto> getByLevel(@RequestParam("level") User.Level level) {
        return lawArticleService.getArticlesByLevel(level);
    }


    @Operation(summary = "법률 콘텐츠 상세 조회", description = "ID를 기준으로 법률 콘텐츠 원문 및 요약을 반환한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 콘텐츠 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LawArticleResponseDto> getById(
            @Parameter(description = "법률 콘텐츠 ID", example = "1")
            @PathVariable Long id
    ) {
        return lawArticleService.getArticleById(id)
                .map(article -> {
                    LawArticleResponseDto dto = LawArticleResponseDto.builder()
                            .id(article.getId())
                            .theme(article.getTheme())
                            .content(article.getContent())
                            .aiSummary(article.getAiSummary())
                            .level(article.getLevel())
                            .contentId(contentRepository.findByExternalIdAndCategory(article.getId(), "LAW")
                                    .map(Content::getId)
                                    .orElse(null)) // 없을 경우 null 허용
                            .build();
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "법률 콘텐츠 CSV 업로드", description = "CSV 파일을 업로드하여 콘텐츠를 DB에 저장한다. (content 컬럼 기준)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일 파싱 실패")
    })
    @PostMapping("/upload")
    public ResponseEntity<LawArticleUploadResponseDto> uploadCsv(
            @Parameter(description = "CSV 파일", required = true)
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return ResponseEntity.ok(lawArticleService.uploadCsv(file));
    }

    @Operation(summary = "법률 콘텐츠 요약 요청", description = "AI를 통해 콘텐츠를 요약하고 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 및 저장 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID 없음"),
            @ApiResponse(responseCode = "500", description = "요약 서버 오류")
    })
    @PostMapping("/{id}/summarize")
    public ResponseEntity<LawArticleResponseDto> summarizeById(@PathVariable Long id) {
        return ResponseEntity.ok(lawArticleService.summarizeAndUpdate(id));
    }

}
