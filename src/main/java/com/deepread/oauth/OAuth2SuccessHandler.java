package com.deepread.oauth;

import com.deepread.entity.User;
import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // OAuth 인증을 마친 사용자 정보
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        // JWT 생성(발급)
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        // RefreshToken DB 저장
        refreshTokenService.saveRefreshToken(user, refreshToken, jwtUtil.getRefreshTokenExpiration());

        // SecurityContext에 User 객체 직접 등록 (인증 처리 완료)
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
          user, // principal
          null, // credentials 없음
          user.getAuthorities() // 권한
        );
        SecurityContextHolder.getContext().setAuthentication(userAuth);

        // 모바일 앱으로 리디렉션
        String redirectUrl = "myapp://oauth2/redirect"
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        response.sendRedirect(redirectUrl);
    }
}
