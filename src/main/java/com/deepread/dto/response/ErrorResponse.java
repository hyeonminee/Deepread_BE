package com.deepread.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;
}
