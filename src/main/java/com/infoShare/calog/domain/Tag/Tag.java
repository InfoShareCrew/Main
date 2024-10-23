package com.infoShare.calog.domain.Tag;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@Entity
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;

    // 게시글 수를 위한 추가 필드
    @Transient // 이 필드는 데이터베이스에 저장되지 않도록 설정
    private int articleCount;
}