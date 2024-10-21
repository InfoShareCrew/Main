package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Notice.Notice;
import com.infoShare.calog.domain.Suggestion.Suggestion;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Comment extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Notice notice;

    @ManyToOne
    private Suggestion suggestion;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

}
