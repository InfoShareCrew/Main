package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.BoardType.BoardType;
import com.infoShare.calog.domain.Comment.Comment;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

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
    private BoardType boardType;

    // TODO: 유저 엔티티 생기면 추천, 글쓴이 추가하기
}
