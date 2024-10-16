package com.infoShare.calog.domain.MajorCategory;

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

    public MajorCategory findById(Short id) {
        return majorCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid major category ID"));
    }

    public MajorCategory create(String name) {
        MajorCategory category = new MajorCategory();
        category.setName(name);
        return majorCategoryRepository.save(category);
    }
}