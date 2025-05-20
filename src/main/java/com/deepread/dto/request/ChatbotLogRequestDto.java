package com.deepread.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatbotLogRequestDto {

    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @NotBlank(message = "질문은 비어 있을 수 없습니다.")
    private String question;

    @NotBlank(message = "응답은 비어 있을 수 없습니다.")
    private String response;
}
