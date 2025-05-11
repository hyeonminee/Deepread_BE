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
                        .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class) // 필터 등록
                .build();  // 체이닝의 끝은 build()
    }
}