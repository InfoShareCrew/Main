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
            boardCategoryRepository.save(new BoardCategory(1, "공지사항"));
            boardCategoryRepository.save(new BoardCategory(2, "건의게시판"));
            boardCategoryRepository.save(new BoardCategory(3, "자유게시판"));
        }
    }
}
