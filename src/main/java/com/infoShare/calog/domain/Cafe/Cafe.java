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

    @ManyToOne
    private BoardCategory boardCategory;

    @ManyToOne
    private SiteUser maneger;

    private String profileImg;

    @ManyToMany
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "cafe")
    private List<Suggestion> suggestions;

}