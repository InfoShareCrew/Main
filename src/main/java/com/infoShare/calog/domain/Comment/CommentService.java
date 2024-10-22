package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.Notice.Notice;
import com.infoShare.calog.domain.Suggestion.Suggestion;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(Article article, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
    }

    public void createSuggestionComment(Suggestion suggestion, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setSuggestion(suggestion);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
    }
    public void createNoticeComment(Notice notice,  String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setNotice(notice);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
    }


    public Comment getComment(Long id) {
        Optional<Comment> optionalComment = this.commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            return optionalComment.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        this.commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public void vote(Comment comment, SiteUser siteUser) {
        comment.getVoter().add(siteUser);
        this.commentRepository.save(comment);
    }


    public void cancelVote(Comment comment, SiteUser siteUser) {
        comment.getVoter().remove(siteUser);
        this.commentRepository.save(comment);
    } //추천 취소


}
