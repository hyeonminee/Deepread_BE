package com.deepread.oauth;

import com.deepread.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class CustomPrincipal implements OAuth2User {

    private final User user;

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName(); // OAuth2User 인터페이스의 getName() 구현
    }

    public User.Level getLevel() {
        return user.getLevel();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // OAuth2User는 권한 목록을 반드시 반환해야 함
        return user.getAuthorities(); // User 클래스의 UserDetails 구현 사용
    }

    @Override
    public Map<String, Object> getAttributes() {
        // 필요한 경우 사용자 속성 map 제공, 여기선 간단하게 빈 맵 반환
        return Collections.emptyMap();
    }
}
