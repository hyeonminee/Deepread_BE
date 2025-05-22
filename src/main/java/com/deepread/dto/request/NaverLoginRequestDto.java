package com.deepread.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class NaverLoginRequestDto {
    private Map<String, Object> naverProfileResult;
    private String accessToken;
}
