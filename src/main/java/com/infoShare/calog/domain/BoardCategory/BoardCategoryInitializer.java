package com.infoShare.calog.domain.BoardCategory;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class BoardCategoryInitializer {
    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    @PostConstruct
    public void init() {
        if (boardCategoryRepository.count() == 0) {
            boardCategoryRepository.save(BoardCategory.builder().name("공지사항").build());
            boardCategoryRepository.save(BoardCategory.builder().name("건의게시판").build());
            boardCategoryRepository.save(BoardCategory.builder().name("자유게시판").build());
        }
    }
}