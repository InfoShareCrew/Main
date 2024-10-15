package com.infoShare.calog.domain.MinorCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinorCategoryRepository extends JpaRepository<MinorCategory,Long> {
}
