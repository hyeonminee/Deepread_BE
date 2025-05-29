package com.deepread.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.deepread.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @Schema(description = "사용자 ID", example = "42")
    private Long id;

    @Schema(description = "사용자 이름", example = "이순신")
    private String name;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String imageUrl;

    @Schema(description = "문해력 수준", example = "INTERMEDIATE")
    private User.Level level;
}
