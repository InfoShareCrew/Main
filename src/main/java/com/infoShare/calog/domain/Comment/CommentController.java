package com.infoShare.calog.domain.Comment;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.function.BiConsumer;

@Controller
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;

    @PostMapping("create/{id}")
    public String create(Model model, @Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable(value = "id")Integer id) {
        Article article = this.articleService.getArticleById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
        }
        this.commentService.createComment(article, commentForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

}
