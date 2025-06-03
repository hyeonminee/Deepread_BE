package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawArticleUploadResponseDto {

    @Schema(description = "성공적으로 저장된 콘텐츠 수", example = "85")
    private int successCount;

    @Schema(description = "실패한 콘텐츠 수", example = "3")
    private int failureCount;
}
