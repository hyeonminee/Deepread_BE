package com.deepread.service;

import com.deepread.entity.User;
import com.deepread.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. 사용자 프로필 조회
    public Optional<User> getUserProfile(Long userId) {
        return userRepository.findById(userId);
    }

    // 2. 사용자 레벨 변경
    public boolean updateLevel(Long userId, User.Level newLevel) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUser_level(newLevel);
            user.setUpdatedAt(java.time.LocalDateTime.now());
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
