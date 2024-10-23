package com.infoShare.calog.domain.Tag;

import jakarta.persistence.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByName(String name); // 태그조회
    Page<Tag> findByNameContaining(String keyword, Pageable pageable); // 태그 검색

}
