package com.infoShare.calog.domain.BoardCategory;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Cafe.Cafe;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "boardCategory")
    private List<Cafe> cafe;
}
