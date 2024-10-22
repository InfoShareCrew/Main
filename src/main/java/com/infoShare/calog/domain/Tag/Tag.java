package com.infoShare.calog.domain.Tag;

import com.infoShare.calog.domain.Article.Article;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;
}
