package com.deepread.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "뉴스 기사 응답 DTO")
public class NewsArticleResponseDto {
    @Schema(description = "뉴스 ID", example = "1")
    private Long id;

    @Schema(description = "뉴스 카테고리 (예: 경제, 사회)", example = "경제")
    private String category;

    @Schema(description = "뉴스 제목", example = "삼성전자, 2분기 실적 발표")
    private String title;

    @Schema(description = "원문 기사 내용")
    private String content;

    @Schema(description = "AI 요약 결과")
    private String aiSummary;

    @Schema(description = "기사 원문 URL", example = "https://www.yna.co.kr/view/123456")
    private String originalUrl;

    @Schema(description = "기사 수집 일시", example = "2025-06-01T07:30:00")
    private LocalDateTime createdAt;
}
