package com.infoShare.calog.domain.Category;

import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<Category> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.categoryRepository.findAll(pageable);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public Category getCategoryById(short id) {
        Optional<Category> majorCategory = this.categoryRepository.findById(id);
        if (majorCategory.isPresent()) {
            return majorCategory.get();
        } else {
            throw new DataNotFoundException("MajorCategory not found");
        }
    }

    public Category findById(Short id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid major category ID"));
    }

    public Category create(String majorCategory,String minorCategory, SiteUser author) {
        Category category = new Category();
        category.setMajorCategory(majorCategory);
        category.setMinorCategory(minorCategory);
        category.setAuthor(author);
        category.setCreatedDate(LocalDateTime.now());
        return categoryRepository.save(category);
    }


    public void modify(Category category, String majorCategory,String minorCategory ){
        category.setMajorCategory(majorCategory);
        category.setMinorCategory(minorCategory);
        category.setModifiedDate(LocalDateTime.now());
        this.categoryRepository.save(category);
    }

    public void delete(Category category) {
        this.categoryRepository.delete(category);
    }

    public Page<Category> searchCategories(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));
        return categoryRepository.findByMajorCategoryContainingOrMinorCategoryContaining(keyword, keyword, pageable);
    }

    public List<Category> getCategoriesByUser(SiteUser author) {
        return categoryRepository.findByAuthor(author);
    }

}