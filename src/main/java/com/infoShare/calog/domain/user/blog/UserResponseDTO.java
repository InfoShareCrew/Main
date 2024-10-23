package com.infoShare.calog.domain.user.blog;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private String nickname;
    private String email;
    private String profileImg;
}

