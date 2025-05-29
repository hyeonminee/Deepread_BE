package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
public class NaverLoginRequestDto {

    @Schema(description = "Naver 프로필 응답 결과")
    private Map<String, Object> naverProfileResult;

    @Schema(description = "Naver 액세스 토큰", example = "AAAAONL4...YYc")
    private String accessToken;
}