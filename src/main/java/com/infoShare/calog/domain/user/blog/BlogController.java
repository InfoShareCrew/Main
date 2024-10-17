package com.infoShare.calog.domain.user.blog;

import com.infoShare.calog.domain.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
        SiteUser siteUser = blogService.getUserBlog(principal.getName());
        model.addAttribute("user", siteUser);
        return "blog_view"; // 블로그 뷰 페이지
    }

    @PreAuthorize("isAuthenticated()") // 이 메서드는 인증된 사용자만 접근 가능
    @PostMapping("/edit")
    public String editBlog(@Valid BlogForm blogForm, @AuthenticationPrincipal SiteUser siteUser, Model model) {
        // 블로그 폼에서 값을 가져옴
        String intro = blogForm.getIntro();
        String address = blogForm.getAddress();
        String nickname = blogForm.getNickname();

        // 사용자 프로필 업데이트 호출
        blogService.updateUserBlog(siteUser, intro, address, nickname);

        return String.format("redirect:/blog"); // 블로그 페이지로 리디렉션
    }
}
