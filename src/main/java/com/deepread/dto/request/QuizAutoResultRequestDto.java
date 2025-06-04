package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAutoResultRequestDto {

    @Schema(description = "사용자 ID", example = "7")
    @NotNull
    private Long userId;

    @Schema(description = "사용자의 답변 리스트 (문제ID + 선택 보기번호)")
    @NotNull
    private List<AnswerSubmission> answers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerSubmission {
        private Long questionId;
        private Integer selectedOption;
    }
}
