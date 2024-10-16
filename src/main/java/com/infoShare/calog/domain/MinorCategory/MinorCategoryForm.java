package com.infoShare.calog.domain.MinorCategory;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinorCategoryForm {
    @NotEmpty(message = "소분류 카테고리 이름은 필수사항입니다.")
    private String name;

    private Short majorCategoryId;
}
