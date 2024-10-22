package com.infoShare.calog.domain.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    // 태그를 생성하거나 기존 태그를 반환
    public Tag getOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    return tagRepository.save(newTag);
                });
    }

    // 여러 태그를 처리하는 메서드
    public Set<Tag> processTags(String tagsString) {
        Set<Tag> tags = new HashSet<>();
        if (tagsString != null && !tagsString.trim().isEmpty()) {
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
}
