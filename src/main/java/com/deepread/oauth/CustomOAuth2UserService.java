package com.deepread.oauth;

import com.deepread.entity.User;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// 사용자 정보(OAuth2User)를 받아와 우리 시스템의 사용자(User)와 연결하거나 저장하기 위한 서비스

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId(); // "kakao", "naver", "google"
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 각 플랫폼별 사용자 식별 정보 파싱
        String socialId = extractSocialId(provider, attributes);

        // 기존 사용자 확인 또는 새로 저장
        User user = userRepository.findBySocialId(socialId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setSocialId(socialId);
                    newUser.setSocialProvider(User.SocialProvider.valueOf(provider));
                    newUser.setLevel(User.Level.초급); // 기본값
                    return userRepository.save(newUser);
                });

        return new CustomOAuth2User(user, attributes);
    }

    private String extractSocialId(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                return String.valueOf(attributes.get("id"));
            }
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return String.valueOf(response.get("id"));
            }
            case "google" -> {
                return String.valueOf(attributes.get("sub"));
            }
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}
