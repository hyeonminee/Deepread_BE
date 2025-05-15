package com.deepread.service;

import com.deepread.entity.User;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 사용자 프로필 조회
    public Optional<User> getUserProfile(Long userId) {
        return userRepository.findById(userId);
    }

    // 사용자 문해력 레벨 변경
    public boolean updateLevel(Long userId, User.Level newLevel) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setLevel(newLevel);
                    user.setUpdatedAt(LocalDateTime.now());
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

}
