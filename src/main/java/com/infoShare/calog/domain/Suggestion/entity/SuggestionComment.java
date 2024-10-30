package com.infoShare.calog.domain.Suggestion.entity;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SuggestionComment extends BaseEntity {
    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne
    private SiteUser author;

    private Long suggestionId;
}
