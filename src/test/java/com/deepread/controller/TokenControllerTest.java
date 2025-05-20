package com.deepread.controller;

import com.deepread.entity.User;
import com.deepread.oauth.jwt.JwtUtil;
import com.deepread.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TokenController.class)
@AutoConfigureMockMvc(addFilters = false)
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("AccessToken 재발급 성공")
    void reissueToken_success() throws Exception {
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";

        User mockUser = new User();
        mockUser.setId(1L);

        when(refreshTokenService.getUserByRefreshToken(refreshToken)).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateAccessToken("1")).thenReturn(newAccessToken);

        mockMvc.perform(post("/api/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("refreshToken", refreshToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken));
    }

    @Test
    @DisplayName("AccessToken 재발급 실패 - 유효하지 않은 Refresh Token")
    void reissueToken_invalidToken() throws Exception {
        String invalidToken = "invalid-refresh-token";

        when(refreshTokenService.getUserByRefreshToken(invalidToken)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("refreshToken", invalidToken))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or expired refresh token"));
    }

    @Test
    @DisplayName("로그아웃 성공 - Refresh Token 제거")
    void logout_success() throws Exception {
        String refreshToken = "some-refresh-token";

        doNothing().when(refreshTokenService).delete(refreshToken);

        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("refreshToken", refreshToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        verify(refreshTokenService, times(1)).delete(refreshToken);
    }
}
