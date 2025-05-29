package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoLoginRequestDto {

    @Schema(description = "Kakao 프로필 응답 결과")
    private Map<String, Object> kakaoProfileResult;

    @Schema(description = "Kakao 액세스 토큰", example = "O9xC3x...C5lTg")
    private String accessToken;
}