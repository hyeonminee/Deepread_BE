package com.deepread.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisQuestionResponseDto {
    private Long id;
    private String type;
    private String passage;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
