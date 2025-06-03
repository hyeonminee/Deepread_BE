package com.deepread.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawArticleUploadRequestDto {
    private Long id;
    private String theme;
    private String question;
    private String answer;
}
