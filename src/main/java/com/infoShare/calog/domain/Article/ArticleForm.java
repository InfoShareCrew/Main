package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.Category.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleForm {
    @NotEmpty(message = "제목은 필수사항입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수사항입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수사항입니다.")
    private Category category;

    private String tags = ""; // 해시태그 입력 필드
}
