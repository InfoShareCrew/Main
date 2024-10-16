package com.infoShare.calog.domain.MajorCategory;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorCategoryService {

    private final MajorCategoryRepository majorCategoryRepository;

    public List<MajorCategory> findAll() {
        return majorCategoryRepository.findAll();
    }


    public MajorCategory getMajorCategoryById(short id) {
        Optional<MajorCategory> majorCategory = this.majorCategoryRepository.findById(id);
        if (majorCategory.isPresent()) {
            return majorCategory.get();
        } else {
            throw new DataNotFoundException("MajorCategory not found");
        }
    }

    public MajorCategory findById(Short id) {
        return majorCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid major category ID"));
    }

    public MajorCategory create(String name) {
        MajorCategory category = new MajorCategory();
        category.setName(name);
        category.setCreateDate(LocalDateTime.now());
        return majorCategoryRepository.save(category);
    }


    public void modifyMajorCategory(MajorCategory majorCategory, String name) {
        majorCategory.setName(name);
        majorCategory.setModifyDate(LocalDateTime.now());
        this.majorCategoryRepository.save(majorCategory);
    }

    public void deleteMajorCategory(MajorCategory majorCategory) {
        this.majorCategoryRepository.delete(majorCategory);
    }
}