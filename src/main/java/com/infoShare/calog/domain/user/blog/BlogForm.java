package com.infoShare.calog.domain.user.blog;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogForm {
    @NotEmpty(message = "닉네임은 필수입력사항입니다.")
    private String nickname;

    private String profileImg;

    private String intro;

    private String address;
}
