package com.infoShare.calog.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
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
            userService.create(userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getNickname());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login_form";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        SiteUser user = userService.findByEmail(email);
        if (user != null && userService.validateUser(email, password)) {
            // 로그인 성공 처리
            return "redirect:/"; // 성공 시 리다이렉트할 URL
        } else {
            // 로그인 실패 처리
            model.addAttribute("error", true);
            return "login_form"; // 로그인 페이지로 다시 리턴
        }
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

    @GetMapping("/password")
    public String findPassword(UserPasswordForm userPasswordForm) {
        return "modify_password";
    }

    @GetMapping("/personal-info")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public SiteUser returnName(Principal principal) {
        return this.userService.findByEmail(principal.getName());
    }

    @GetMapping("/personal/{userId}")
    @PreAuthorize("isAuthenticated()")
    public String personalPage(Model model, @PathVariable(value = "userId") Long id, Principal principal) {
        SiteUser user = this.userService.findByEmail(principal.getName());

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
        return "personal";
    }
}