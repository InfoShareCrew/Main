package com.infoShare.calog.domain.user.blog;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import com.infoShare.calog.global.Util.UtilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final UtilService utilService;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    @GetMapping("/personal-info")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Object getUserOb(Principal principal) {
        return this.userService.findByEmail(principal.getName());
    }

    @GetMapping("/{userEmail}")
    public String userBlog(Model model, @PathVariable(value = "userEmail") String email) {
        SiteUser user = this.userService.findByEmail(email);

        // 문자열로 저장되어 있는 사용자의 개인링크 첨부를 알고리즘으로 풀어 리스트로 html에 첨부
        String address = user.getAddress();
        List<String> addressList = new ArrayList<>();
        if (address != null && user.getAddress().contains("::")) {
            String[] parts = address.split("::");
            addressList = new ArrayList<>(Arrays.asList(parts));
            if (!addressList.isEmpty()) { // 리스트가 비어 있지 않은 경우에만
                addressList.remove(addressList.size() - 1); // 마지막 비어있는 문자열 제거
            }
        } else {
            addressList.add(address);
        }

        model.addAttribute("addressList", addressList);
        model.addAttribute("user", user);
        model.addAttribute("fileDirPath", fileDirPath);
        return "blog_view";
    }

    @GetMapping("/edit/{userEmail}")
    @PreAuthorize("isAuthenticated()")
    public String modifyUserProfile(BlogForm blogForm, Principal principal) {
        SiteUser user = userService.findByEmail(principal.getName());
        blogForm.setNickname(user.getNickname());
        blogForm.setIntro(user.getIntro());
        blogForm.setAddress(user.getAddress());
        blogForm.setProfileImg(user.getProfileImg());
        return "blog_prof_form";
    }

    @PostMapping("/edit/{userEmail}")
    @PreAuthorize("isAuthenticated()")
    public String editBlog(@Valid BlogForm blogForm,
                           @PathVariable(value = "userEmail") String email,
                           @RequestParam(value = "profile-img") MultipartFile image,
                           BindingResult bindingResult,
                            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "blog_prof_form";
        }

        // TODO: 프로필 사진 변경하지 않고 저장할 시 빈 파일 저장되는 거 디버깅하기
        String profileImg = this.utilService.saveImage("user", image);
        SiteUser siteUser = userService.findByEmail(principal.getName());
        blogService.updateUserProfile(siteUser,
                                        blogForm.getIntro(),
                                        blogForm.getAddress(),
                                        blogForm.getNickname(),
                                        profileImg); // 블로그 서비스에서 업데이트 호출
        return String.format("redirect:/blog/%s", email); // 블로그 페이지로 리디렉션
    }
}
