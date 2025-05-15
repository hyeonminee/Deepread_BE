package com.deepread.controller;

import com.deepread.dto.request.UpdateLevelRequestDto;
import com.deepread.dto.response.UserResponseDto;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(summary = "프로필 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "프로필 반환")
    @GetMapping("/profile")
    public UserResponseDto getUserProfile(@AuthenticationPrincipal User authenticatedUser) {
        User user = userService.getUserProfile(authenticatedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        return modelMapper.map(user, UserResponseDto.class);
    }

    @Operation(summary = "레벨 수정", description = "현재 사용자의 문해력 수준(Level)을 수정한다.")
    @ApiResponse(responseCode = "200", description = "레벨 수정 성공 메시지 반환")
    @PutMapping("/level")
    public String updateUserLevel(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody UpdateLevelRequestDto dto) {

        boolean updated = userService.updateLevel(authenticatedUser.getId(), dto.getNewLevel());
        return updated ? "User level updated successfully" : "User not found";
    }

}
