package com.deepread.dto.response;

import com.deepread.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalArticleResponseDto {

    @Schema(description = "의료 콘텐츠 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "심근경색 초기 증상")
    private String theme;

    @Schema(description = "원문 내용", example = "심근경색은 흉통, 호흡곤란 등으로 시작...")
    private String content;


    @Schema(description = "AI 요약 결과", example = "심근경색은 초기 흉통과 관련된 질병이다.")
    private String aiSummary;

    @Schema(description = "문해력 수준", example = "고급")
    private User.Level level;

    @Schema(description = "Summary 저장용 콘텐츠 ID (Content 테이블 PK)", example = "123")
    private Long contentId;
}
