package com.infoShare.calog.domain.MinorCategory;

import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MinorCategoryService {

    private final MinorCategoryRepository minorCategoryRepository;

    public List<MinorCategory> findAll() {
        return minorCategoryRepository.findAll();
    }

    public MinorCategory getMinorCategoryById(short id) {
        Optional<MinorCategory> minorCategory = this.minorCategoryRepository.findById(id);
        if (minorCategory.isPresent()) {
            return minorCategory.get();
        } else {
            throw new DataNotFoundException("MinorCategory not found");
        }
    }


    public MinorCategory findById(Short id) {
        return minorCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid minor category ID"));
    }

    public MinorCategory create(String name, MajorCategory majorCategory) {
        MinorCategory category = new MinorCategory();
        category.setName(name);
        category.setCreateDate(LocalDateTime.now());
        category.setMajorCategory(majorCategory);
        return minorCategoryRepository.save(category);
    }

    public void modifyMinorCategory(MinorCategory minorCategory, String name) {
        minorCategory.setName(name);
        minorCategory.setModifyDate(LocalDateTime.now());
        this.minorCategoryRepository.save(minorCategory);
    }


    public void deleteMinorCategory(MinorCategory minorCategory) {
        this.minorCategoryRepository.delete(minorCategory);
    }
}