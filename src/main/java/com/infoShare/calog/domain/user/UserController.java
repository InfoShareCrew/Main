package com.infoShare.calog.domain.user;

import com.infoShare.calog.domain.user.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

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
            userService.join(userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getNickname());
            emailService.send(userCreateForm.getEmail(), "infoShare 서비스 가입을 환영합니다!", "회원가입 환영 메일");
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String email, @RequestParam UserRole role) {
        userService.assignRoleToUser(email, role);
        return ResponseEntity.ok("권한이 부여되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/password")
    public String ModifyPasswordForm(Model model) {
        // 사용자 객체 생성
        UserFindPasswordForm userFindPasswordForm = new UserFindPasswordForm();
        model.addAttribute("userFindPasswordForm", userFindPasswordForm);
        return "modify_password"; // 비밀번호 변경 폼 뷰
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public String showModifyPasswordForm(@ModelAttribute("userFindPasswordForm") @Valid UserFindPasswordForm userFindPasswordForm,
                                         BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "modify_password";
        }

        // 현재 로그인한 사용자의 이메일을 가져오기
        String email = principal.getName(); // 현재 인증된 사용자의 이메일

        Optional<SiteUser> optionalUser = this.userService.findUserByEmail(email);

        // 사용자가 존재하지 않을 경우 처리
        if (optionalUser.isEmpty()) {
            bindingResult.reject("error.siteUser", "사용자를 찾을 수 없습니다."); // 에러 메시지 추가
            return "modify_password"; // 에러가 있을 경우 다시 폼으로 돌아감
        }

        // 비밀번호 확인 로직
        if (!userFindPasswordForm.getPassword().equals(userFindPasswordForm.getPassword_check())) {
            bindingResult.rejectValue("password_check", "error.siteUser", "비밀번호가 일치하지 않습니다.");
            return "modify_password"; // 에러가 있을 경우 다시 폼으로 돌아감
        }

        SiteUser siteUser = optionalUser.get();
        this.userService.modifypassword(siteUser.getId(), userFindPasswordForm.getPassword()); // 사용자 ID를 넘김
        return "redirect:/"; // 비밀번호 변경 후 리다이렉트
    }
}