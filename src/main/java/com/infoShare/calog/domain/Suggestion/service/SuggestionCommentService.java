package com.infoShare.calog.domain.Suggestion.service;

import java.util.*;
import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Suggestion.entity.SuggestionComment;
import com.infoShare.calog.domain.Suggestion.repository.SuggestionCommentRepository;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SuggestionCommentService {
    private final SuggestionCommentRepository suggestionCommentRepository;

    public List<SuggestionComment> list() {
        return this.suggestionCommentRepository.findAll();
    }

    public SuggestionComment getComment(Long commentId) {
        Optional<SuggestionComment> comment = this.suggestionCommentRepository.findById(commentId);

        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변이 존재하지 않습니다.");
        } else {
            return this.suggestionCommentRepository.findById(commentId).get();
        }
    }

    public void create(Long suggestionID, String title, String content, SiteUser user, Cafe cafe) {
        SuggestionComment comment = new SuggestionComment();
        comment.setTitle(title);
        comment.setContent(content);
        comment.setCafe(cafe);
        comment.setAuthor(user);
        comment.setSuggestionId(suggestionID);
        this.suggestionCommentRepository.save(comment);
    }
}
