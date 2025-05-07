package com.deepread.controller;

import com.deepread.entity.User;
import com.deepread.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자의 프로필 조회
    @GetMapping("/{userId}")
    public User getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    // 사용자의 문해력 수준(level) 수정
    @PutMapping("/{userId}/level")
    public String updateUserLevel(@PathVariable Long userId, @RequestParam String level) {
        boolean updated = userService.updateLevel(userId, User.Level.valueOf(level));
        return updated ? "User level updated successfully" : "User not found";
    }

}
