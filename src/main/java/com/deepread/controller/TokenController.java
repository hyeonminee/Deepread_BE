package com.deepread.controller;

import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.service.RefreshTokenService;
import com.deepread.entity.User;
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

    // 액세스 토큰 재발급 API
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

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        refreshTokenService.delete(refreshToken);
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
