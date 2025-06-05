package com.deepread.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedArticlesDto {
    private List<LawArticleResponseDto> lawArticles;
    private List<MedicalArticleResponseDto> medicalArticles;
    private List<NewsArticleResponseDto> newsArticles;
}
