package com.deepread.controller;

import com.deepread.dto.request.UpdateLevelRequestDto;
import com.deepread.entity.User;
import com.deepread.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    // SecurityContext에 User 엔티티를 직접 주입
    private void setAuthenticatedUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    @DisplayName("사용자 프로필 조회 성공")
    void getUserProfile_success() throws Exception {
        setAuthenticatedUser(1L, "테스트 유저");

        User user = new User();
        user.setId(1L);
        user.setName("테스트 유저");

        Mockito.when(userService.getUserProfile(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 유저"));
    }

    @Test
    @DisplayName("레벨 업데이트 성공")
    void updateLevel_success() throws Exception {
        setAuthenticatedUser(1L, "테스트 유저");

        UpdateLevelRequestDto dto = new UpdateLevelRequestDto();
        dto.setNewLevel(User.Level.고급);

        Mockito.when(userService.updateLevel(1L, User.Level.고급)).thenReturn(true);

        mockMvc.perform(put("/api/user/level")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User level updated successfully"));
    }

    @Test
    @DisplayName("레벨 업데이트 실패 - 사용자 없음")
    void updateLevel_userNotFound() throws Exception {
        setAuthenticatedUser(99L, "없는 유저");

        UpdateLevelRequestDto dto = new UpdateLevelRequestDto();
        dto.setNewLevel(User.Level.초급);

        Mockito.when(userService.updateLevel(anyLong(), Mockito.any())).thenReturn(false);

        mockMvc.perform(put("/api/user/level")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User not found"));
    }
}
