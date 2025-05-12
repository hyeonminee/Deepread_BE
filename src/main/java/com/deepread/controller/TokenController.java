package com.deepread.controller;

import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.service.RefreshTokenService;
import com.deepread.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급
    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받는다.")
    @ApiResponse(responseCode = "200", description = "액세스 토큰 재발급 완료")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        return refreshTokenService.getUserByRefreshToken(refreshToken)
                .map(user -> {
                    String newAccessToken = jwtUtil.generateAccessToken(user.getId().toString());
                    return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired refresh token")));
    }

    // 로그아웃 요청 처리
    @Operation(summary = "로그아웃", description = "리프레시 토큰을 삭제하여 로그아웃한다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        refreshTokenService.delete(refreshToken);
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
