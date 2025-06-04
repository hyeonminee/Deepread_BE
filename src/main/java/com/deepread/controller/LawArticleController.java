package com.deepread.controller;

import com.deepread.dto.response.LawArticleResponseDto;
import com.deepread.dto.response.LawArticleUploadResponseDto;
import com.deepread.service.LawArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Law", description = "법률 콘텐츠 관련 API")
@RestController
@RequestMapping("/api/law")
@RequiredArgsConstructor
public class LawArticleController {

    private final LawArticleService lawArticleService;

    @Operation(summary = "법률 콘텐츠 전체 조회", description = "모든 법률 카드뉴스 콘텐츠 목록을 반환한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public List<LawArticleResponseDto> getAll() {
        return lawArticleService.getAllArticles();
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
                .map(ResponseEntity::ok)
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
