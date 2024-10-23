package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.Category.Category;
import com.infoShare.calog.domain.Comment.Comment;
import com.infoShare.calog.domain.Tag.Tag;
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
    private Category Category;

    @ManyToMany
    Set<SiteUser> voter;

    @ManyToMany  // 해시태그
    @JoinTable(name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

}

// 카페 카테고리 게시판