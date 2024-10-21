package com.infoShare.calog.domain.Category;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {
    @NotEmpty(message = "대분류 카테고리 이름은 필수사항입니다.")
    private String majorCategory;


    @NotEmpty(message = "소분류 카테고리 이름은 필수사항입니다.")
    private String minorCategory;
}
