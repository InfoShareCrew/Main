package com.infoShare.calog.domain.PopularPosts;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Cafe.CafeService; // 필요에 따라 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/popularpost")
public class PopularPostsController {

    private final ArticleService articleService;
    private final CafeService cafeService; // 필요에 따라 추가

    @GetMapping
    public String getPopularPosts(Model model) {
        // 인기글 5개 가져오기
        List<Article> popularArticles = articleService.getPopularArticles(5);

        // 모델에 추가
        model.addAttribute("popularArticles", popularArticles);

        // 인기글을 보여줄 뷰 이름 반환
        return "popular_posts"; // 예: popular_posts.html
    }
}
