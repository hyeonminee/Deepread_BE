package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * ETRI 어휘 정보 API 응답을 담는 DTO 클래스
 */
@Getter
@Builder
public class MeansResponseDto {

    @Schema(description = "단어 자체", example = "사과")
    private String word;

    @Schema(description = "뜻 목록 (각 의미의 정의 및 품사 포함)")
    private List<MeaningDetail> meanings;

    @Schema(description = "유의어 목록", example = "[\"사죄\", \"해명\"]")
    private List<String> synonym;

    @Schema(description = "반의어 목록", example = "[\"비난\", \"책망\"]")
    private List<String> antonym;

    @Getter
    @Builder
    public static class MeaningDetail {
        @Schema(description = "품사", example = "명사")
        private String pos;

        @Schema(description = "의미 정의", example = "잘못한 일이나 실수에 대해 용서를 빎.")
        private String definition;
    }
}
