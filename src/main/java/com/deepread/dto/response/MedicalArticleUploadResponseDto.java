package com.deepread.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalArticleUploadResponseDto {
    private int successCount;
    private int failureCount;
}
