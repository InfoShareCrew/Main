package com.infoShare.calog.domain.BoardCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Integer> {
    BoardCategory findByName(String boardCategoryName);

    // 카페 ID와 카테고리 이름으로 검색하는 메소드 추가
    @Query("SELECT bc FROM BoardCategory bc WHERE bc.name = :categoryName AND bc.cafe.id = :cafeId")
    BoardCategory getCategoryByNameAndCafeId(@Param("categoryName") String categoryName, @Param("cafeId") Long cafeId);
}
