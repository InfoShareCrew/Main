package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(Article article, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        this.commentRepository.save(comment);
    }
}
