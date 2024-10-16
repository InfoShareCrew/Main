package com.infoShare.calog.domain.user.blog;

import com.infoShare.calog.domain.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    public String viewBlog(Model model, Principal principal) {
        SiteUser siteUser = blogService.getUserProfile(principal.getName());
        model.addAttribute("user", siteUser);
        return "blog_view"; // 블로그 뷰 페이지
    }

    @PostMapping("/edit")
    public String editBlog(@Valid BlogForm blogForm, @AuthenticationPrincipal SiteUser siteUser, String intro, String address, String nickname) {
        blogService.updateUserProfile(siteUser, intro, address, blogForm.getNickname()); // 블로그 서비스에서 업데이트 호출
        return "redirect:/blog"; // 블로그 페이지로 리디렉션
    }
}
