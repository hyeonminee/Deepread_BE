package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageStatisticsDto {

    @Schema(description = "읽은 콘텐츠 수", example = "10")
    private int readContentCount;

    @Schema(description = "완료한 퀴즈 수", example = "5")
    private int quizCompletedCount;

    @Schema(description = "학습한 단어 수", example = "12")
    private int learnedWordCount;

    @Schema(description = "연속 학습일 수", example = "3")
    private int streak;
}

