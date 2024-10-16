package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.MajorCategory.MajorCategoryService;
import com.infoShare.calog.domain.MinorCategory.MinorCategoryService;
import com.infoShare.calog.domain.Suggestion.Suggestion;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("cafe")
public class CafeController {
    private final CafeService cafeService;
    private final MajorCategoryService majorCategoryService;
    private final MinorCategoryService minorCategoryService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Cafe> cafes = cafeService.findAll();
        model.addAttribute("cafes", cafes);
        return "cafe_list";
    }

    @GetMapping("/create")
    public String create(CafeForm cafeForm) {
        return "cafe_form";
    }

    @PostMapping("/create")
    public String create(@Valid CafeForm cafeForm,
                         BindingResult bindingResult,
                         Principal principal) {

        if (bindingResult.hasErrors()) {
            return "cafe_form";
        }

        String email = principal.getName();
        SiteUser manager = userService.getUser(email);

        this.cafeService.create(cafeForm.getTitle(), cafeForm.getMajorCategoryId(), cafeForm.getMinorCategoryId(), manager);
        return "redirect:/cafe/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Long id) {
        Cafe cafe = this.cafeService.getCafeById(id);
        model.addAttribute("cafe", cafe);
        return "cafe_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid CafeForm cafeForm, BindingResult bindingResult,
                         Principal principal,@PathVariable("id") Long id){

        if (bindingResult.hasErrors()){
            return "cafe_form";
        }
        Cafe cafe = this.cafeService.getCafeById(id);
        if (!cafe.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.cafeService.modifyCafe(cafe,cafeForm.getTitle());
        return String.format("redirect:/cafe/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteCafe(Principal principal, @PathVariable("id") Long id) {
        Cafe cafe = this.cafeService.getCafeById(id);
        if (!cafe.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.cafeService.deleteCafe(cafe);
        return "redirect:/";
    }
}