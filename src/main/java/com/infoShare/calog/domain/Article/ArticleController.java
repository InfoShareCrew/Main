package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.Category.Category;
import com.infoShare.calog.domain.Category.CategoryService;
import com.infoShare.calog.domain.Comment.CommentForm;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Article> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.articleService.searchArticles(kw, page);
        } else {
            // 기본 목록
            paging = this.articleService.getList(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw); // 검색어를 모델에 추가
        return "article_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Long id, CommentForm commentForm, Principal principal) {
        Article article = this.articleService.getArticleById(id);
        model.addAttribute("article", article);
        this.articleService.viewUp(article);

        if (principal != null) {
            SiteUser user = userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }

        // 선택한 카테고리 정보 추가
        Category majorCategory = article.getMajorCategory();
        model.addAttribute("selectedCategoryId", majorCategory != null ? majorCategory.getId() : null);
        model.addAttribute("categories", categoryService.getAllCategories()); // 모든 카테고리 목록 가져오기

        return "article_detail";
    }

    @GetMapping("/create")
    public String create(ArticleForm articleForm, Model model) {
        model.addAttribute("categories", categoryService.getList(0).getContent()); // 카테고리 가져오기
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        SiteUser author = this.userService.getUser(principal.getName());
        Category category = this.categoryService.getCategoryById(articleForm.getCategoryId());
        this.articleService.createArticle(articleForm.getTitle(), articleForm.getContent(), author, category);
        return "redirect:/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyArticle(ArticleForm articleForm, @PathVariable("id") Long id,Model model, Principal principal){
        Article article = this.articleService.getArticleById(id);
        if(!article.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());
        articleForm.setCategoryId(articleForm.getCategoryId());
        model.addAttribute("articleForm",articleForm);
        model.addAttribute("categories",categoryService.getAllCategories());
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
        Category category = this.categoryService.getCategoryById(articleForm.getCategoryId()); // 카테고리 추가
        this.articleService.modify(article, articleForm.getTitle(), articleForm.getContent(), category); // 수정 메서드 호출
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

    @GetMapping("/blog/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id); // 이 메서드가 null을 반환할 수 있는지 확인
        if (article != null) {
            model.addAttribute("article", article);
        }
        return "blog_view"; // 템플릿 이름
    }
}