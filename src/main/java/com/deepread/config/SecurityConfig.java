package com.deepread.config;

import com.deepread.oauth.CustomOAuth2UserService;
import com.deepread.oauth.OAuth2SuccessHandler;
import com.deepread.oauth.jwt.JwtAuthenticationFilter;
import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2SuccessHandler oAuth2SuccessHandler,
                                           JwtUtil jwtUtil,
                                           UserRepository userRepository) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ 퍼블릭 접근 허용 경로
                        .requestMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()

                        // USER 전용 API
                        .requestMatchers("/api/user/**").hasRole("USER")

                        // ADMIN 전용 API (필요 시)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 나머지는 인증만 필요
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
