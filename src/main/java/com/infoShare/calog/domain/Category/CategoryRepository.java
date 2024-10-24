package com.infoShare.calog.domain.Category;

import com.infoShare.calog.domain.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Short> {
    Page<Category> findAll(Pageable pagement);
    Page<Category> findByMajorCategoryContainingOrMinorCategoryContaining(String majorCategory, String minorCategory, Pageable pageable);
    List<Category> findByAuthor(SiteUser author); // 사용자에 따른 카테고리 조회 메서드
}