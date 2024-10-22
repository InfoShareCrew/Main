package com.infoShare.calog.domain.Article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pagement);
    Page<Article> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
