package com.infoShare.calog.domain.BoardCategory;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardCategoryService {
    private final BoardCategoryRepository boardCategoryRepository;

    public BoardCategory getNoticeCategory() {
        return this.boardCategoryRepository.getReferenceById(1);
    }

    public List<BoardCategory> getList() {
        return this.boardCategoryRepository.findAll();
    }

    public BoardCategory getCategoryByName(String boardCategoryName) {
        return this.boardCategoryRepository.findByName(boardCategoryName);
    }

    public BoardCategory getCategoryByNameAndCafeId(String boardName, Long cafeId) {
        return this.boardCategoryRepository.getCategoryByNameAndCafeId(boardName, cafeId);
    };

    public void create(Cafe cafe, String boardName) {
        BoardCategory boardCategory = new BoardCategory();
        boardCategory.setName(boardName);
        boardCategory.setCafe(cafe);
        this.boardCategoryRepository.save(boardCategory);
    }
}
