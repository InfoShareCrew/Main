package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Category.Category;
import com.infoShare.calog.domain.Comment.Comment;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Article extends BaseEntity {
    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long view = 0L;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    private SiteUser author;

    @ManyToOne
    @JoinColumn(name = "board_category_id")
    private BoardCategory boardCategory;

    @ManyToOne
    @JoinColumn(name = "cafe")
    private Cafe cafe;

    @ManyToMany
    Set<SiteUser> voter;

}
