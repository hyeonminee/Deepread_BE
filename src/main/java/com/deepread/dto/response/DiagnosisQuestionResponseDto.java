package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisQuestionResponseDto {

    @Schema(description = "질문 ID", example = "1001")
    private Long id;

    @Schema(description = "문제 유형", example = "A")
    private String type;

    @Schema(description = "지문", example = "지문 내용 예시...")
    private String passage;

    @Schema(description = "질문", example = "이 단어의 의미는 무엇인가요?")
    private String question;

    @Schema(description = "선택지 1", example = "정답")
    private String option1;

    @Schema(description = "선택지 2", example = "오답1")
    private String option2;

    @Schema(description = "선택지 3", example = "오답2")
    private String option3;

    @Schema(description = "선택지 4", example = "오답3")
    private String option4;
}

