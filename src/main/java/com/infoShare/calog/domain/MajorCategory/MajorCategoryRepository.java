package com.infoShare.calog.domain.MajorCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorCategoryRepository extends JpaRepository<MajorCategory,Short> {
}
