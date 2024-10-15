package com.infoShare.calog.domain.MaiorCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorCategoryRepository extends JpaRepository<MajorCategory,Long> {
}
