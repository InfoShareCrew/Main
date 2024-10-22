package com.infoShare.calog.domain.Notice;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.Cafe.Cafe;
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
public class Notice extends BaseEntity {
    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long view = 0L;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    private SiteUser author;

    @ManyToOne
    private Cafe cafe;

    @ManyToMany
    Set<SiteUser> voter;
}