package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    @Column(unique = true, nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_level")
    private Level level = Level.초급;

    private String imageUrl;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum SocialProvider {
        kakao, naver, google
    }

    public enum Level {
        초급, 중급, 고급
    }

    // UserDetails 구현 필수 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한이 필요 없으면 빈 리스트
    }

    @Override
    public String getUsername() {
        return this.socialId; // 로그인 식별자 (socialId)
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호 없음 (소셜 로그인)
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return !this.isDeleted; }
}
