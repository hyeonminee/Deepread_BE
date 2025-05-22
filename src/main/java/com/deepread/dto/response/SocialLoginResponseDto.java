package com.deepread.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String email;
}
