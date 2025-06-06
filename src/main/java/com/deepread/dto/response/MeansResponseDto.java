package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeansResponseDto {

    @Schema(description = "단어", example = "사과")
    private String word;

    @Schema(description = "품사", example = "명사")
    private String pos;

    @Schema(description = "뜻풀이", example = "사람이나 사물의 잘못을 꾸짖거나 노여워함.")
    private String definition;

    @Schema(description = "어휘 구분", example = "고유어, 한자어 등")
    private String type;

    @Schema(description = "자세한 뜻, 예문 등을 볼 수 있는 링크", example = "https://stdict.korean.go.kr/search/searchView.do?word_no=404765&searchKeywordTo=3")
    private String link;
}
