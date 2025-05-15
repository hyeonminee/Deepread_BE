package com.deepread.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatbotLogRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    private String question;

    @NotNull
    private String response;
}
