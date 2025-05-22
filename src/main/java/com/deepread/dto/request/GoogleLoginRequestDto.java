package com.deepread.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleLoginRequestDto {
    private Map<String, Object> googleProfileResult;
    private String accessToken;
}
