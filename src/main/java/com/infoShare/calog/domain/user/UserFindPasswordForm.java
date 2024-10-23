package com.infoShare.calog.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindPasswordForm {
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String email;

  @NotEmpty(message = "인증코드를 입력해 주세요.")
  private String authCode;

  @NotEmpty(message = "비밀번호를 입력해 주세요.")
  private String password;

  @NotEmpty(message = "비밀번호 확인을 입력해 주세요.")
  private String password_check;
}
