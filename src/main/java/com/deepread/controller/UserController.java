package com.deepread.controller;

import com.deepread.dto.UpdateLevelDto;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 자신의 프로필 정보 조회
    @Operation(summary = "프로필 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "프로필 반환")
    @GetMapping("/profile")
    public User getUserProfile(@AuthenticationPrincipal User authenticatedUser) {
        return userService.getUserProfile(authenticatedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 사용자의 문해력 레벨(level) 수정
    @Operation(summary = "레벨 수정", description = "현재 사용자의 문해력 수준(Level)을 수정한다.")
    @ApiResponse(responseCode = "200", description = "레벨 수정 성공 메시지 반환")
    @PutMapping("/level")
    public String updateUserLevel(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody UpdateLevelDto dto) {

        boolean updated = userService.updateLevel(authenticatedUser.getId(), dto.getNewLevel());
        return updated ? "User level updated successfully" : "User not found";
    }

}
