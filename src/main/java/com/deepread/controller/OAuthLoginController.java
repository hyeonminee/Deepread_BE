package com.deepread.controller;

import com.deepread.dto.request.KakaoLoginRequestDto;
import com.deepread.dto.request.GoogleLoginRequestDto;
import com.deepread.dto.request.NaverLoginRequestDto;
import com.deepread.dto.response.SocialLoginResponseDto;
import com.deepread.entity.User;
import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.repository.UserRepository;
import com.deepread.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "소셜 로그인", description = "OAuth 기반 로그인 처리")
@RestController
@RequestMapping("/oauth/callback")
@RequiredArgsConstructor
public class OAuthLoginController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "카카오 로그인", description = "카카오 사용자 정보를 통해 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequestDto dto) {
        Map<String, Object> kakaoProfile = dto.getKakaoProfileResult();
        String kakaoId = String.valueOf(kakaoProfile.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoProfile.get("kakao_account");
        String nickname = ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname").toString();
        String email = kakaoAccount.get("email").toString();
        String profileImage = ((Map<String, Object>) kakaoAccount.get("profile")).get("profile_image_url").toString();

        return processUserLogin(kakaoId, User.SocialProvider.kakao, nickname, email, profileImage);
    }

    @Operation(summary = "구글 로그인", description = "구글 사용자 정보를 통해 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequestDto dto) {
        Map<String, Object> profile = dto.getGoogleProfileResult();
        String googleId = String.valueOf(profile.get("sub"));
        String email = profile.get("email").toString();
        String name = profile.get("name").toString();
        String picture = profile.get("picture").toString();

        return processUserLogin(googleId, User.SocialProvider.google, name, email, picture);
    }

    @Operation(summary = "네이버 로그인", description = "네이버 사용자 정보를 통해 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/naver")
    public ResponseEntity<?> naverLogin(@RequestBody NaverLoginRequestDto dto) {
        Map<String, Object> profile = dto.getNaverProfileResult();
        Map<String, Object> response = (Map<String, Object>) profile.get("response");
        String naverId = String.valueOf(response.get("id"));
        String name = response.get("name").toString();
        String email = response.get("email").toString();
        String picture = response.get("profile_image").toString();

        return processUserLogin(naverId, User.SocialProvider.naver, name, email, picture);
    }

    private ResponseEntity<SocialLoginResponseDto> processUserLogin(String socialId, User.SocialProvider provider, String name, String email, String imageUrl) {
        User user = userRepository.findBySocialId(socialId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setSocialId(socialId);
                    newUser.setSocialProvider(provider);
                    newUser.setName(name);
                    newUser.setImageUrl(imageUrl);
                    return userRepository.save(newUser);
                });

        String jwtAccessToken = jwtUtil.generateAccessToken(user.getId().toString());
        String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        refreshTokenService.saveRefreshToken(user, jwtRefreshToken, jwtUtil.getRefreshTokenExpiration());

        return ResponseEntity.ok(new SocialLoginResponseDto(
                jwtAccessToken,
                jwtRefreshToken,
                user.getName(),
                email
        ));
    }
}