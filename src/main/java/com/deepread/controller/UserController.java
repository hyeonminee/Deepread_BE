package com.deepread.controller;

import com.deepread.dto.request.UpdateLevelRequestDto;
import com.deepread.dto.response.UserResponseDto;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 반환 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/profile")
    public UserResponseDto getUserProfile(@AuthenticationPrincipal User user) {
        return modelMapper.map(userService.getUserProfile(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다.")),
                UserResponseDto.class);
    }

    @Operation(summary = "레벨 수정", description = "현재 로그인한 사용자의 문해력 수준(Level)을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레벨 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/level")
    public String updateUserLevel(@AuthenticationPrincipal User user,
                                  @RequestBody @Valid UpdateLevelRequestDto dto) {
        boolean updated = userService.updateLevel(user.getId(), dto.getNewLevel());
        return updated ? "User level updated successfully" : "User not found";
    }

}
