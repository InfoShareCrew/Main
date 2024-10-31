package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Notice.NoticeService;
import com.infoShare.calog.domain.Suggestion.service.SuggestionService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(Model model,
                         @Valid CommentForm commentForm,
                         @PathVariable(value = "id") Long id,
                         BindingResult bindingResult,
                         Principal principal,
                         HttpServletRequest request) {
        Article article = this.articleService.getArticleById(id);
        SiteUser author = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
        }

        this.commentService.createComment(article, commentForm.getContent(), author);

        String referer = request.getHeader("Referer");
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/defaultPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(CommentForm commentForm, @PathVariable(value = "id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm,
                                @PathVariable("id") Long id,
                                BindingResult bindingResult,
                                Principal principal,
                                HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.commentService.modify(comment, commentForm.getContent());

        String referer = request.getHeader("Referer");
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/defaultPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id, HttpServletRequest request) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);

        String referer = request.getHeader("Referer");
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/defaultPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/suggestion/delete/{id}")
    public String commentSuggestionDelete(Principal principal, @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/suggestion/detail/%s", comment.getSuggestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/delete/{id}")
    public String commentNoticeDelete(Principal principal, @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/notice/detail/%s", comment.getNotice().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String commentVote(@PathVariable("id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.commentService.vote(comment, siteUser);

        Comment votedComment = this.commentService.getComment(id);
        Integer count = votedComment.getVoter().size();

        return count.toString();
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/unvote/{id}")  //추천취소
    @ResponseBody
    public String cancelVote(@PathVariable("id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.commentService.cancelVote(comment, siteUser);

        Comment votedComment = this.commentService.getComment(id);
        Integer count = comment.getVoter().size();
        return count.toString();
    }
}
