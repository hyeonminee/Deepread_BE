package com.deepread.dto.response;

import com.deepread.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 포함한 생성자 추가
public class UserResponseDto {
    private Long id;
    private String name;
    private String imageUrl;
    private User.Level level;
}
