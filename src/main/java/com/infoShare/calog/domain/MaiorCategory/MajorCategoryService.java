package com.infoShare.calog.domain.MaiorCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorCategoryService {

    private final MajorCategoryRepository majorCategoryRepository;

    public List<MajorCategory> findAll() {
        return majorCategoryRepository.findAll();
    }

    public MajorCategory findById(Long id) {
        return majorCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid major category ID"));
    }

    public MajorCategory create(String title) {
        MajorCategory category = new MajorCategory();
        category.setTitle(title);
        return majorCategoryRepository.save(category);
    }
}