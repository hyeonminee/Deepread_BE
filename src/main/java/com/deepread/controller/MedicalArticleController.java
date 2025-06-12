package com.deepread.controller;

import com.deepread.dto.response.MedicalArticleResponseDto;
import com.deepread.dto.response.MedicalArticleUploadResponseDto;
import com.deepread.entity.User;
import com.deepread.oauth.CustomPrincipal;
import com.deepread.service.MedicalArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Medical", description = "의료 콘텐츠 관련 API")
@RestController
@RequestMapping("/api/medical")
@RequiredArgsConstructor
public class MedicalArticleController {

    private final MedicalArticleService medicalArticleService;

    @Operation(summary = "의료 콘텐츠 조회", description = "로그인한 사용자의 레벨에 해당하는 콘텐츠만 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping
    public List<MedicalArticleResponseDto> getByUserLevel(@AuthenticationPrincipal CustomPrincipal principal) {
        User.Level level = principal.getUser().getLevel();
        return medicalArticleService.getArticlesByLevel(level);
    }

    @GetMapping("/level")
    public List<MedicalArticleResponseDto> getByLevel(@RequestParam("level") User.Level level) {
        return medicalArticleService.getArticlesByLevel(level);
    }


    @Operation(summary = "의료 콘텐츠 상세 조회", description = "ID를 기반으로 의료 콘텐츠 원문을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 콘텐츠 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalArticleResponseDto> getById(
            @Parameter(description = "의료 콘텐츠 ID", example = "1")
            @PathVariable Long id
    ) {
        return medicalArticleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "의료 콘텐츠 CSV 업로드", description = "CSV 파일을 업로드하여 콘텐츠를 DB에 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일 파싱 실패")
    })
    @PostMapping("/upload")
    public ResponseEntity<MedicalArticleUploadResponseDto> uploadCsv(
            @Parameter(description = "CSV 파일", required = true)
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return ResponseEntity.ok(medicalArticleService.uploadCsv(file));
    }

    @Operation(summary = "의료 콘텐츠 AI 요약 요청", description = "AI 서버에 원문 콘텐츠를 전송해 요약문을 생성하고 DB에 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 콘텐츠 없음"),
            @ApiResponse(responseCode = "500", description = "요약 처리 중 서버 오류")
    })
    @PostMapping("/{id}/summarize")
    public ResponseEntity<MedicalArticleResponseDto> summarizeAndUpdate(
            @Parameter(description = "의료 콘텐츠 ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(medicalArticleService.summarizeAndUpdate(id));
    }
}
