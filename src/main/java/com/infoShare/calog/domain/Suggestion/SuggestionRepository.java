package com.infoShare.calog.domain.Suggestion;

import com.infoShare.calog.domain.Notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    Page<Suggestion> findByCafeId(Pageable pagement, Long cafeId);

    Page<Suggestion> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    @Query("SELECT a FROM Notice a " +
            "JOIN a.cafe c " +  // cafe와의 조인을 추가
            "WHERE (a.title LIKE %:title% OR a.content LIKE %:content%) " +
            "AND c.id = :cafeId")  // cafeId 조건 추가
    Page<Suggestion> findByTitleContainingOrContentContainingAndCafeId(
            @Param("title") String title,
            @Param("content") String content,
            @Param("cafeId") Long cafeId,  // cafeId 파라미터 추가
            Pageable pageable);
}
