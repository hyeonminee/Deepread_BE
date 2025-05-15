package com.deepread.dto.response;

import com.deepread.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String imageUrl;
    private User.Level level;
}
