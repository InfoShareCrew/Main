package com.infoShare.calog.domain.Tag;

import com.infoShare.calog.domain.Article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    // 태그를 생성하거나 기존 태그를 반환
    public Tag getOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    return tagRepository.save(newTag);
                });
    }

    // 여러 태그 문자열을 처리
    public Set<Tag> processTags(String tagsString) {
        Set<Tag> tags = new HashSet<>();
        if (tagsString != null && !tagsString.trim().isEmpty()) {
            // , 로 구분
            String[] tagNames = tagsString.split(",");
            for (String tagName : tagNames) {
                String trimmedName = tagName.trim();
                // '#' 기호가 없으면 추가
                if (!trimmedName.startsWith("#")) {
                    trimmedName = "#" + trimmedName;
                }
                tags.add(getOrCreateTag(trimmedName));
            }
        }
        return tags;
    }

    // 태그 조회 및 태그에 대한 게시글 수 반환
    public List<Tag> getAllTags() {
        List<Tag> tags = tagRepository.findAll(Sort.by(Sort.Order.asc("name")));
        for (Tag tag : tags) {
            int count = articleRepository.countByTags(tag); // 각 태그에 대한 게시글 수 계산
            tag.setArticleCount(count); // 태그에 게시글 수 설정
        }
        return tags;
    }


    // 페이지네이션
    public Page<Tag> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.tagRepository.findAll(pageable);
    }

    //태그를 검색
    public Page<Tag> searchTags(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));
        return tagRepository.findByNameContaining(keyword, pageable);
    }

}
