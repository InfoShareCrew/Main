package com.infoShare.calog.domain.Cafe;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeForm {
    @NotEmpty(message = "제목은 필수사항입니다.")
    private String title;

    @NotEmpty(message = "대분류 카테고리는 필수사항입니다.")
    private Short majorCategoryId;

    @NotEmpty(message = "소뷴류 카테고리는 필수사항입니다.")
    private Short minorCategoryId;
}
