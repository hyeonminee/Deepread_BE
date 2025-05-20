package com.deepread.service;

import com.deepread.entity.User;
import com.deepread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("사용자 프로필 조회 성공")
    void getUserProfile_success() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("홍길동");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserProfile(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals("홍길동", result.get().getName());
    }

    @Test
    @DisplayName("사용자 프로필 조회 실패 (없는 ID)")
    void getUserProfile_userNotFound() {
        // given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.getUserProfile(userId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("사용자 레벨 변경 성공")
    void updateLevel_success() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setLevel(User.Level.초급);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        boolean updated = userService.updateLevel(userId, User.Level.고급);

        // then
        assertTrue(updated);
        assertEquals(User.Level.고급, user.getLevel());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("사용자 레벨 변경 실패 - 사용자 없음")
    void updateLevel_userNotFound() {
        // given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        boolean result = userService.updateLevel(userId, User.Level.중급);

        // then
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
}
