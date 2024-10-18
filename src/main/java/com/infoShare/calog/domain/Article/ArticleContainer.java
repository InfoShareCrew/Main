package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.Comment.CommentForm;
import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("article")
public class ArticleContainer {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Article> paging = this.articleService.getList(page);
        model.addAttribute("paging", paging);
        return "article_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Long id, CommentForm commentForm, Principal principal) {
        Article article = this.articleService.getArticleById(id);
        model.addAttribute("article", article);
        this.articleService.viewUp(article);

        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }

        return "article_detail";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(ArticleForm articleForm) {
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        SiteUser author = this.userService.getUser(principal.getName());
        this.articleService.createArticle(articleForm.getTitle(), articleForm.getContent(),author);
        return "redirect:/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyArticle(ArticleForm articleForm, @PathVariable("id") Long id, Principal principal){
        Article article = this.articleService.getArticleById(id);
        if(!article.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyArticle(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                Principal principal, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            return "article_form";
        }
        Article article = this.articleService.getArticleById(id);
        if(!article.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.articleService.modify(article,articleForm.getTitle(),articleForm.getContent());
        return String.format("redirect:/article/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteArticle(Principal principal, @PathVariable("id") Long id){
        Article article = this.articleService.getArticleById(id);
        if(!article.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.articleService.delete(article);
        return "redirect:/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String articleVote(@PathVariable("id") Long id, Principal principal) {
        Article article = this.articleService.getArticleById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.articleService.vote(article, siteUser);

        Article votedArticle = this.articleService.getArticleById(id);
        Integer count = votedArticle.getVoter().size();
        return count.toString();
    }
}