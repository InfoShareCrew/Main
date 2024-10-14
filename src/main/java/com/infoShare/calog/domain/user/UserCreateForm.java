package com.infoShare.calog.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password1;

    @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    private String password2;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;
}
