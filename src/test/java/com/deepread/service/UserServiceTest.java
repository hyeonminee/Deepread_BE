package com.deepread.service;

import com.deepread.entity.User;
import com.deepread.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateLevel_success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userService.updateLevel(1L, User.Level.중급);

        assertTrue(result);
        assertEquals(User.Level.중급, user.getLevel());
        verify(userRepository).save(user);
    }

    @Test
    void updateLevel_userNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        boolean result = userService.updateLevel(2L, User.Level.고급);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
}
