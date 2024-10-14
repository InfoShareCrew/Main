package com.infoShare.calog.domain.Suggestion;

import com.infoShare.calog.domain.BoardType.BoardType;
import com.infoShare.calog.domain.Comment.Comment;
import com.infoShare.calog.domain.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private Long view = 0L;

    @OneToMany(mappedBy = "suggestion", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    private BoardType boardType;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

}
