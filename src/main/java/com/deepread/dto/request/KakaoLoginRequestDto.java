package com.deepread.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoLoginRequestDto {
    private Map<String, Object> kakaoProfileResult;
    private String accessToken;
}
