package com.infoShare.calog.domain.user;

import com.infoShare.calog.domain.user.email.EmailService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/password")
    public String findPassword(UserFindPasswordForm userPasswordForm) {
        return "modify_password";
    }
}
