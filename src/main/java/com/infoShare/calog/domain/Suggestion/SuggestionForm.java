package com.infoShare.calog.domain.Suggestion;

import com.infoShare.calog.domain.Suggestion.entity.Suggestion;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuggestionForm {
    @NotEmpty(message = "제목은 필수사항입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수사항입니다.")
    private String content;

    private Long suggestionId;
}
