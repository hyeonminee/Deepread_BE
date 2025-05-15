package com.deepread.controller;

import com.deepread.entity.User;
import com.deepread.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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

    private void setAuthenticatedUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("사용자 프로필 조회 성공")
    void getUserProfile_success() throws Exception {
        // given
        setAuthenticatedUser(1L, "테스트 유저");

        User user = new User();
        user.setId(1L);
        user.setName("테스트 유저");

        Mockito.when(userService.getUserProfile(1L)).thenReturn(Optional.of(user));

        // when & then
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 유저"));
    }

    @Test
    @DisplayName("레벨 업데이트 성공")
    void updateLevel_success() throws Exception {
        // given
        setAuthenticatedUser(1L, "테스트 유저");

        UpdateLevelDto dto = new UpdateLevelDto();
        dto.setNewLevel(User.Level.고급);

        Mockito.when(userService.updateLevel(1L, User.Level.고급))
                .thenReturn(true);

        // when & then
        mockMvc.perform(put("/api/user/level")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User level updated successfully"));
    }
}
