package com.deepread.service;

import com.deepread.entity.RefreshToken;
import com.deepread.entity.User;
import com.deepread.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Refresh 토큰 저장
    public void saveRefreshToken(User user, String token, long expirationMillis) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(token);
        refreshToken.setExpiredAt(LocalDateTime.now().plus(Duration.ofMillis(expirationMillis)));
        refreshTokenRepository.save(refreshToken);
    }

    // 토큰으로 사용자 조회 (유효성 포함)
    public Optional<User> getUserByRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .filter(rt -> rt.getExpiredAt().isAfter(LocalDateTime.now()))
                .map(RefreshToken::getUser);
    }

    // 로그아웃 시 Refresh 토큰 삭제
    public void delete(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
