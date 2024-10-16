package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import com.infoShare.calog.domain.MajorCategory.MajorCategoryService;
import com.infoShare.calog.domain.MinorCategory.MinorCategory;
import com.infoShare.calog.domain.MinorCategory.MinorCategoryService;
import com.infoShare.calog.domain.Suggestion.Suggestion;
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
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MajorCategoryService majorCategoryService;
    private final MinorCategoryService minorCategoryService;

    public Page<Cafe> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.cafeRepository.findAll(pageable);
    }

    public Cafe getCafeById(Long id) {
        Optional<Cafe> cafe = this.cafeRepository.findById(id);
        if (cafe.isPresent()) {
            return cafe.get();
        } else {
            throw new DataNotFoundException("Cafe not found");
        }
    }

    public Cafe findById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cafe ID"));
    }

   /* public Cafe create(String title, Short majorCategoryId, Short minorCategoryId, SiteUser manager) {
        MajorCategory majorCategory = majorCategoryService.findById(majorCategoryId);
        MinorCategory minorCategory = minorCategoryService.findById(minorCategoryId);

        Cafe cafe = new Cafe();
        cafe.setTitle(title);
        cafe.setManager(manager);
        cafe.setMajorCategory(majorCategory);
        cafe.setMinorCategory(minorCategory);

        return cafeRepository.save(cafe);
    }*/

    public Cafe create(String title, String content ,SiteUser author) {
        Cafe cafe = new Cafe();
        cafe.setTitle(title);
        cafe.setContent(content);
        cafe.setAuthor(author);
        return cafeRepository.save(cafe);
    }

    public void modifyCafe(Cafe cafe, String title, String content) {
        cafe.setTitle(title);
        cafe.setContent(content);
        this.cafeRepository.save(cafe);
    }

    public void deleteCafe(Cafe cafe) {
        this.cafeRepository.delete(cafe);
    }


    public void vote(Cafe cafe, SiteUser siteUser) {
        cafe.getVoter().add(siteUser);
        this.cafeRepository.save(cafe);
    }
}
