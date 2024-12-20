package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Category.Category;
import com.infoShare.calog.domain.Tag.Tag;
import com.infoShare.calog.domain.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe,Long> {
    List<Cafe> findAllByManager(SiteUser manager);

    List<Cafe> findByNameContainingIgnoreCase(String name);
}

