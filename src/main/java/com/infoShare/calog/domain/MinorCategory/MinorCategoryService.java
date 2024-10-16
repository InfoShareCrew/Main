package com.infoShare.calog.domain.MinorCategory;

import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinorCategoryService {

    private final MinorCategoryRepository minorCategoryRepository;

    public List<MinorCategory> findAll() {
        return minorCategoryRepository.findAll();
    }

    public MinorCategory findById(Short id) {
        return minorCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid minor category ID"));
    }

    public MinorCategory create(String name, MajorCategory majorCategory) {
        MinorCategory category = new MinorCategory();
        category.setName(name);
        category.setMajorCategory(majorCategory);
        return minorCategoryRepository.save(category);
    }
}