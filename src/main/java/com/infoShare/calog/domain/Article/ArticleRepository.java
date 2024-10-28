package com.infoShare.calog.domain.Article;

import java.util.List;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.Tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pagement);

    Page<Article> findByBoardCategoryName(Pageable pagement, String boardName);

    @Query("SELECT a FROM Article a JOIN a.boardCategory bc " +
            "JOIN a.cafe c " +
            "WHERE bc.name = :boardName " +
            "AND c.id = :cafeId")
    Page<Article> findByBoardCategoryNameAndCafeId(
            Pageable pageable,
            @Param("boardName") String boardName,
            @Param("cafeId") Long cafeId);

    @Query("SELECT a FROM Article a JOIN a.boardCategory bc " +
            "WHERE (a.title LIKE %:title% OR a.content LIKE %:content%) " +
            "AND bc.name = :boardName")
    Page<Article> findByTitleContainingOrContentContainingAndBoardCategoryId(
            @Param("title") String title,
            @Param("content") String content,
            @Param("boardName") String boardName,
            Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.boardCategory bc " +
            "JOIN a.cafe c " +  // cafe와의 조인을 추가
            "WHERE (a.title LIKE %:title% OR a.content LIKE %:content%) " +
            "AND bc.name = :boardName " +
            "AND c.id = :cafeId")  // cafeId 조건 추가
    Page<Article> findByTitleContainingOrContentContainingAndBoardCategoryAndCafeId(
            @Param("title") String title,
            @Param("content") String content,
            @Param("boardName") String boardName,
            @Param("cafeId") Long cafeId,  // cafeId 파라미터 추가
            Pageable pageable);

    Page<Article> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Article> findByTags_Name(String tagName, Pageable pageable);

    int countByTags(Tag tag); // 특정 태그에 속하는 게시글 수

    @Query("SELECT a FROM Article a ORDER BY a.view DESC") // 인기글 가져오기 - 전체
    List<Article> findTopPopularArticles(Pageable pageable);

    @Query("SELECT a FROM Article a " +   // 인기글 가져오기 - 카페
            "WHERE a.cafe.id = :cafeId " +
            "ORDER BY a.view DESC")
    List<Article> findTopPopularArticlesByCafeId(@Param("cafeId") Long cafeId, Pageable pageable);
}

//    @Query(
//            "SELECT a FROM Article a " +
//            "WHERE (a.title LIKE %:title% OR a.content LIKE %:content%) AND a.boardCategory.id = :boardCategoryId")
//    Page<Article> findByTitleContainingOrContentContainingAndBoardCategoryId(
//            String title, String content, Integer boardCategoryId, Pageable pageable);}
