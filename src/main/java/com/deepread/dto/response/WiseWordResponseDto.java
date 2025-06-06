package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "ETRI 어휘 정보 API 전체 응답 DTO")
public class WiseWordResponseDto {

    @Schema(description = "API 응답 코드 (0: 성공, 그 외: 오류)", example = "0")
    private int result;

    @Schema(description = "단어 정보가 포함된 반환 객체")
    private ReturnObject return_object;

    @Getter
    @Setter
    @Schema(description = "단어 반환 객체")
    public static class ReturnObject {

        @Schema(description = "검색된 단어", example = "사과")
        private String Word;

        @Schema(description = "단어의 상세 정보 리스트")
        private List<WordInfo> WordInfo;
    }

    @Getter
    @Setter
    @Schema(description = "단어의 상세 정보")
    public static class WordInfo {

        @Schema(description = "단어 정의", example = "자신의 잘못에 대해 용서를 빎.")
        private String Definition;

        @Schema(description = "품사 (예: 명사, 동사 등)", example = "명사")
        private String POS;

        @Schema(description = "유의어 (JSON 문자열 형태)", example = "[\"사죄\", \"변명\"]")
        private String Synonym;

        @Schema(description = "반의어 (JSON 문자열 형태)", example = "[\"비난\", \"책망\"]")
        private String Antonym;

        @Schema(description = "예문", example = "그는 사과 한 마디 없이 자리를 떴다.")
        private String Example;

        @Schema(description = "한자 표기 또는 어원", example = "謝過")
        private String Origin;
    }
}
