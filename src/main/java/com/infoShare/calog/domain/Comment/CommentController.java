package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Notice.Notice;
import com.infoShare.calog.domain.Notice.NoticeService;
import com.infoShare.calog.domain.Suggestion.Suggestion;
import com.infoShare.calog.domain.Suggestion.SuggestionService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
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
    private final NoticeService noticeService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(Model model, @Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable(value = "id") Long id, Principal principal) {
        Article article = this.articleService.getArticleById(id);
        SiteUser author = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
        }

        this.commentService.createComment(article, commentForm.getContent(), author);
        return String.format("redirect:/article/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/suggestion/create/{id}")
    public String createSuggestionComment(Model model, @Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable(value = "id") Long id, Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        SiteUser author = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("suggestion", suggestion);
        }

        this.commentService.createSuggestionComment(suggestion, commentForm.getContent(), author);
        return String.format("redirect:/suggestion/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/notice/create/{id}")
    public String createNoticeComment(Model model, @Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable(value = "id") Long id, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        SiteUser author = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("notice", notice);
        }

        this.commentService.createNoticeComment(notice, commentForm.getContent(), author);
        return String.format("redirect:/notice/detail/%s", id);
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
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.commentService.modify(comment, commentForm.getContent());

        return String.format("redirect:/article/detail/%s#comment_%s",
                comment.getArticle().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/suggestion/modify/{id}")
    public String modifySuggestion(CommentForm commentForm, @PathVariable(value = "id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/suggestion/modify/{id}")
    public String commentSuggestionModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.commentService.modify(comment, commentForm.getContent());

        return String.format("redirect:/suggestion/detail/%s#comment_%s",
                comment.getSuggestion().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/modify/{id}")
    public String modifyNotice(CommentForm commentForm, @PathVariable(value = "id") Long id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/notice/modify/{id}")
    public String commentNoticeModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                          @PathVariable("id") Long id, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.commentService.modify(comment, commentForm.getContent());

        return String.format("redirect:/notice/detail/%s#comment_%s",
                comment.getNotice().getId(), comment.getId());
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);

        if (!comment.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/article/detail/%s", comment.getArticle().getId());
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
