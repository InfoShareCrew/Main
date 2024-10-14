package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.Comment.CommentForm;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("article")
public class ArticleContainer {
    private final ArticleService articleService;

    @GetMapping("/list")
    public String list(Model model, @ModelAttribute("basedEntity")BaseEntity baseEntity, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="kw", defaultValue = "") String kw) {
        Page<Article> paging = this.articleService.getList(page);
        model.addAttribute("paging", paging);
        return "article_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value="id") Integer id, CommentForm commentForm, Principal principal) {
        Article article = this.articleService.getArticleById(id);
        model.addAttribute("article", article);
        this.articleService.viewUp(article);
        return "article_detail";
    }

    @GetMapping("/create")
    public String create(ArticleForm articleForm) {
        return "article_form";
    }

    @PostMapping("/create")
    public String create(@Valid ArticleForm articleForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "article_form";
        }
        this.articleService.createArticle(articleForm.getTitle(), articleForm.getContent());
        return "redirect:/article/list";
    }

}
