package com.infoShare.calog;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Cafe.CafeService;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ArticleService articleService;
    private final CafeService cafeService;

    //    기본 홈 화면
    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        List<Cafe> cafeList = null;
        List<Cafe> myCafeList = null;
        if (principal != null) {
            cafeList = this.cafeService.getMyList(principal.getName());
            myCafeList = this.cafeService.getOwnList(principal.getName());
        }
        List<Article> hotList = this.articleService.getPopularArticles(5);
        model.addAttribute("cafeList", cafeList);
        model.addAttribute("myCafeList", myCafeList);
        model.addAttribute("hotList", hotList);
        return "index";
    }
}