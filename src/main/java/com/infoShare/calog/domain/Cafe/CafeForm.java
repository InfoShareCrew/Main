package com.infoShare.calog.domain.Cafe;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeForm {
    @NotNull(message = "제목은 필수사항입니다.")
    private String title;

    @NotNull(message = "내용은 필수사항입니다.")
    private String content;

}
