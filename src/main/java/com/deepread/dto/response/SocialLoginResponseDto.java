package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginResponseDto {

    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String accessToken;

    @Schema(description = "JWT 리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String refreshToken;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String username;

    @Schema(description = "사용자 이메일", example = "hong@example.com")
    private String email;
}

