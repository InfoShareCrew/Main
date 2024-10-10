package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Suggestion.Suggestion;
import com.infoShare.calog.domain.Suggestion.SuggestionForm;
import com.infoShare.calog.domain.Suggestion.SuggestionService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final SuggestionService suggestionService;
    private final UserService userService;

    @PostMapping("create/{id}")
    public String create(Model model, @Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable(value = "id")Integer id) {
        Article article = this.articleService.getArticleById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
        }
        this.commentService.createComment(article, commentForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }
    @PostMapping("suggestion/{id}")
    public String create(Model model, @Valid SuggestionForm suggestionForm, BindingResult bindingResult, @PathVariable(value = "id")Integer id) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("suggestion", suggestion);
        }
        this.commentService.createSuggestionComment(suggestion, suggestionForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(CommentForm commentForm, @PathVariable(value = "id")Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if(!comment.getAuthor().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/article/detail/%s#comment_%s",
                comment.getArticle().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/article/detail/%s", comment.getArticle().getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String commentVote(@PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.commentService.vote(comment, siteUser);

        Comment votedComment = this.commentService.getComment(id);
        Integer count = comment.getVoter().size();
        return count.toString();
    }

}
