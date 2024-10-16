package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.MajorCategory.MajorCategoryService;
import com.infoShare.calog.domain.MinorCategory.MinorCategoryService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        if (bindingResult.hasErrors()){
            return "cafe_form";
        }

        String email = principal.getName();
        SiteUser manager = userService.getUser(email);

        this.cafeService.create(cafeForm.getTitle(),cafeForm.getMajorCategoryId(), cafeForm.getMinorCategoryId(),manager);
        return "redirect:/cafe/list";
    }


}