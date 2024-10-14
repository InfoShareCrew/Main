package com.infoShare.calog.domain.user;

import com.infoShare.calog.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            userService.join(userCreateForm.getEmail(), userCreateForm.getNickname(), userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/login/oauth2/callback/kakao")
    public String kakaoCallback(@AuthenticationPrincipal SiteUser siteUser, Model model) {
        String email = siteUser.getEmail();
        String nickname = siteUser.getNickname();

        // UserService를 통해 사용자 정보 처리
        userService.processKakaoUser(email, nickname);

        model.addAttribute("userName", nickname);
        return "welcome"; // 사용자 환영 페이지
    }

    @GetMapping("/login/oauth2/callback/google")
    public String googleCallback(@AuthenticationPrincipal SiteUser siteUser, Model model) {
        String email = siteUser.getEmail();
        String nickname = siteUser.getNickname();

        // UserService를 통해 사용자 정보 처리
        userService.processGoogleUser(email, nickname);

        model.addAttribute("userName", nickname);
        return "welcome"; // 사용자 환영 페이지
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String email, @RequestParam UserRole role) {
        userService.assignRoleToUser(email, role);
        return ResponseEntity.ok("권한이 부여되었습니다.");
    }
}