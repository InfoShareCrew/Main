package com.infoShare.calog.domain.BoardCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardCategoryService {
    private final BoardCategoryRepository boardCategoryRepository;

    public BoardCategory getNoticeCategory() {
        return this.boardCategoryRepository.getReferenceById(1);
    }

    public BoardCategory getSuggestCategory() {
        return this.boardCategoryRepository.getReferenceById(2);
    }

    public List<BoardCategory> getList() {
        return this.boardCategoryRepository.findAll();
    }

    public BoardCategory getCategoryByName(String boardCategoryName) {
        return this.boardCategoryRepository.findByName(boardCategoryName);
    }
}
