package com.deepread.oauth;

import com.deepread.entity.User;
import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    /*
     * 여기서 작성하는 redirect-uri는 application.yml에서 사용하는 redirect-uri와 다름.
     * application.yml -> 소셜 인증 처리를 위한 Spring Security가 자동 사용하는 엔드포인트
     * OAuth2SuccessHandler -> 인증 완료 후 최종적으로 토큰 전달을 위한 redirect-url
     */
    @Value("${custom.redirect.app}")
    private String appRedirectUrl;

    @Value("${custom.redirect.web}")
    private String webRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // OAuth 인증된 사용자 정보 추출
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        // JWT 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        // RefreshToken 저장
        refreshTokenService.saveRefreshToken(user, refreshToken, jwtUtil.getRefreshTokenExpiration());

        // SecurityContext 등록
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(userAuth);

        // 리디렉션 URL 설정 (User-Agent 기반 분기)
        String userAgent = request.getHeader("User-Agent");
        String redirectUrl = (userAgent != null && userAgent.contains("Android"))
                ? appRedirectUrl
                : webRedirectUrl;

        // 토큰 포함하여 리디렉션
        redirectUrl += "?accessToken=" + accessToken + "&refreshToken=" + refreshToken;

        response.sendRedirect(redirectUrl);
    }
}
