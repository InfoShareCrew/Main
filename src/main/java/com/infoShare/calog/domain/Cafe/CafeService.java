package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.MaiorCategory.MajorCategory;
import com.infoShare.calog.domain.MaiorCategory.MajorCategoryService;
import com.infoShare.calog.domain.MinorCategory.MinorCategory;
import com.infoShare.calog.domain.MinorCategory.MinorCategoryService;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MajorCategoryService majorCategoryService;
    private final MinorCategoryService minorCategoryService;

    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    public Cafe findById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cafe ID"));
    }

    public Cafe create(String title, Long majorCategoryId, Long minorCategoryId, SiteUser manager) {
        MajorCategory majorCategory = majorCategoryService.findById(majorCategoryId);
        MinorCategory minorCategory = minorCategoryService.findById(minorCategoryId);

        Cafe cafe = new Cafe();
        cafe.setTitle(title);
        cafe.setCreateDate(LocalDateTime.now());
        cafe.setManager(manager);
        cafe.setMajorCategory(majorCategory);
        cafe.setMinorCategory(minorCategory);

        return cafeRepository.save(cafe);
    }
}
