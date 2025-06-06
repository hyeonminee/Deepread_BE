package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * ETRI 어휘 정보 API 응답을 담는 DTO 클래스
 * 단어의 세부 정보를 포함: 품사, 뜻, 한자, 예문, 유의어, 반의어 등
 */
@Getter
@Builder
public class MeansResponseDto {

    @Schema(description = "단어 자체", example = "사과")
    private String word;

    @Schema(description = "품사 (예: 명사, 동사 등)", example = "명사")
    private String pos;

    @Schema(description = "단어의 정의", example = "잘못한 일이나 실수에 대해 용서를 빎.")
    private String definition;

    @Schema(description = "해당 단어의 한자 표기", example = "謝過")
    private String hanja;

    @Schema(description = "예문", example = "그는 사과 한 마디 없이 나가 버렸다.")
    private String example;

    @Schema(description = "유의어 목록 (JSON 배열 형태의 문자열)", example = "[\"사죄\", \"변명\"]")
    private String synonym;

    @Schema(description = "반의어 목록 (JSON 배열 형태의 문자열)", example = "[\"비난\", \"책망\"]")
    private String antonym;
}
