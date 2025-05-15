package com.deepread.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatbotLogResponseDto {
    private Long id;
    private Long userId;
    private String question;
    private String response;
    private LocalDateTime createdAt;
}
