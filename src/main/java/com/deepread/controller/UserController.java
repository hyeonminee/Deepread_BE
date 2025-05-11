package com.deepread.controller;

import com.deepread.dto.UpdateLevelDto;
import com.deepread.entity.User;
import com.deepread.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자의 프로필 조회
    @GetMapping("/profile")
    public User getUserProfile(@AuthenticationPrincipal User authenticatedUser) {
        return userService.getUserProfile(authenticatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    // 사용자의 문해력 수준(level) 수정
    @PutMapping("/level")
    public String updateUserLevel(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody UpdateLevelDto dto) {

        boolean updated = userService.updateLevel(authenticatedUser.getId(), dto.getNewLevel());
        return updated ? "User level updated successfully" : "User not found";
    }

}
