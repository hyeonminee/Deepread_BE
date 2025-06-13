package com.deepread.dto.response;

import com.deepread.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawArticleResponseDto {

    @Schema(description = "법률 콘텐츠 ID", example = "1")
    private Long id;

    @Schema(description = "법률 주제", example = "기준 중위소득")
    private String theme;

    @Schema(description = "질문+답변 통합 원문 데이터")
    private String content;

    @Schema(description = "AI 생성 요약문", nullable = true)
    private String aiSummary;

    @Schema(description = "문해력 수준", example = "초급")
    private User.Level level;

    @Schema(description = "연결된 콘텐츠 ID (Summary 저장 시 사용)", example = "12")
    private Long contentId;
}
