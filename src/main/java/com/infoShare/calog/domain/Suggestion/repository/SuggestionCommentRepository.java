package com.infoShare.calog.domain.Suggestion.repository;

import com.infoShare.calog.domain.Suggestion.entity.SuggestionComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionCommentRepository extends JpaRepository<SuggestionComment, Long> {
}
