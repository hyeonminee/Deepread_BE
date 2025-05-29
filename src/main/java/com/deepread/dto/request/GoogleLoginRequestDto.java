package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleLoginRequestDto {

    @Schema(description = "Google 프로필 응답 결과")
    private Map<String, Object> googleProfileResult;

    @Schema(description = "Google 액세스 토큰", example = "ya29.a0AfH6SMDJ...XyZ")
    private String accessToken;
}
