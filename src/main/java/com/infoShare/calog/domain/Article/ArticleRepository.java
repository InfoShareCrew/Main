package com.infoShare.calog.domain.Article;

import java.util.List;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pagement);

    @Query("SELECT a FROM Article a JOIN a.boardCategory bc " +
            "WHERE (a.title LIKE %:title% OR a.content LIKE %:content%) " +
            "AND bc.name = '공지사항'")
    Page<Article> findByTitleContainingOrContentContainingAndBoardCategoryId(
            @Param("title") String title,
            @Param("content") String content,
            Pageable pageable);
}

//    @Query(
//            "SELECT a FROM Article a " +
//            "WHERE (a.title LIKE %:title% OR a.content LIKE %:content%) AND a.boardCategory.id = :boardCategoryId")
//    Page<Article> findByTitleContainingOrContentContainingAndBoardCategoryId(
//            String title, String content, Integer boardCategoryId, Pageable pageable);}
