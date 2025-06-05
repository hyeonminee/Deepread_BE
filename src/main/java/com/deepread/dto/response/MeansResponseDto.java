package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "단어 의미 응답 DTO")
public class MeansResponseDto {

    @Schema(description = "단어", example = "사과")
    private String word;

    @Schema(description = "구분 (고유어/한자어 등)", example = "고유어")
    private String pos;

    @Schema(description = "뜻풀이", example = "먹을 수 있는 붉은색 열매")
    private String definition;

    @Schema(description = "문형", example = "~을 하다")
    private String pattern;

    @Schema(description = "용례", example = "사과를 먹었다.")
    private String example;

    @Schema(description = "관용구 또는 속담", example = "사과도 때가 있다.")
    private String proverb;

    @Schema(description = "용법 또는 사용범위", example = "일상어")
    private String usage;
}
