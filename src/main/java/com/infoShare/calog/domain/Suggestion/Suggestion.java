package com.infoShare.calog.domain.Suggestion;

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
public class Suggestion extends BaseEntity {
    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long view = 0L;

    @OneToMany(mappedBy = "suggestion", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

}
