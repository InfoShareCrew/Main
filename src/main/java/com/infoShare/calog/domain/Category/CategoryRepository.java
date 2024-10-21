package com.infoShare.calog.domain.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Short> {
    Page<Category> findAll(Pageable pagement);
    Page<Category> findByMajorCategoryContainingOrMinorCategoryContaining(String majorCategory, String minorCategory, Pageable pageable);
}
