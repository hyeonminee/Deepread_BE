package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    // 사용자 역할 (권한)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public enum SocialProvider {
        kakao, naver, google
    }

    public enum Level {
        초급, 중급, 고급
    }

    public enum Role {
        USER, ADMIN
    }

    // Spring Security 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.socialId;
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인이라서 패스워드 없음
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !Boolean.TRUE.equals(this.isDeleted);
    }
}
>>>>>>> 5544aefb8273249396e3ead3b63041e0cb15dccf
