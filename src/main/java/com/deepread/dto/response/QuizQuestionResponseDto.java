package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizQuestionResponseDto {

    @Schema(description = "문제 ID", example = "1")
    private Long id;

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
