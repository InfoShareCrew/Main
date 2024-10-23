package com.infoShare.calog.domain.Article;

import java.util.List;

import com.infoShare.calog.domain.Tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pagement);
    Page<Article> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    Page<Article> findByTags_Name(String tagName, Pageable pageable);
    int countByTags(Tag tag); // 특정 태그에 속하는 게시글 수
}
