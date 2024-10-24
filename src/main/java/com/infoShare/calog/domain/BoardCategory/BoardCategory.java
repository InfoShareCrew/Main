package com.infoShare.calog.domain.BoardCategory;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Cafe.Cafe;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String name;

    @OneToMany(mappedBy = "boardCategory")
    private List<Article> articles;

    // BoardCategory와 Cafe 간의 ManyToOne 관계 설정
    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
}
