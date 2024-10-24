package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.Category.Category;
import com.infoShare.calog.domain.Suggestion.Suggestion;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
public class Cafe extends BaseEntity {
    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String intro;

    @ManyToOne
    private Category category;

    @OneToMany
    private Set<BoardCategory> boardCategory;

    @ManyToOne(optional = false) // 매니저가 필수일 경우
    private SiteUser manager;

    @ManyToOne
    private SiteUser author;

    private String profileImg;

    @OneToMany(mappedBy = "cafe") // Users와 Cafe의 관계 설정
    private Set<SiteUser> users; // SiteUser가 @Entity여야 합니다.

    @ManyToMany
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "cafe")
    private List<Suggestion> suggestions;

}