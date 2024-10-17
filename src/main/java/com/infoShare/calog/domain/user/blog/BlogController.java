package com.infoShare.calog.domain.user.blog;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;

    @GetMapping("/personal-info")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public SiteUser getUserOb(Principal principal) {
        return this.userService.findByEmail(principal.getName()).get();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public String userBlog(Model model, @PathVariable(value = "userId") Long id, Principal principal) {
        SiteUser user = this.userService.findByEmail(principal.getName()).get();

        // 문자열로 저장되어 있는 사용자의 개인링크 첨부를 알고리즘으로 풀어 리스트로 html에 첨부
        List<String> addressList = new ArrayList<>();
        if (user.getAddress() != null) {
            String[] parts = user.getAddress().split("::");
            addressList = new ArrayList<>(Arrays.asList(parts));
            if (!addressList.isEmpty()) { // 리스트가 비어 있지 않은 경우에만
                addressList.remove(addressList.size() - 1); // 마지막 비어있는 문자열 제거
            }
        }

        model.addAttribute("addressList", addressList);
        model.addAttribute("user", user);
        return "blog_view";
    }

    @PostMapping("/edit")
    public String editBlog(@Valid BlogForm blogForm, @AuthenticationPrincipal SiteUser siteUser, String intro, String address, String nickname) {
        blogService.updateUserProfile(siteUser, intro, address, blogForm.getNickname()); // 블로그 서비스에서 업데이트 호출
        return "redirect:/blog"; // 블로그 페이지로 리디렉션
    }
}
