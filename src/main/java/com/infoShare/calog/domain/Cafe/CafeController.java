package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.MaiorCategory.MajorCategory;
import com.infoShare.calog.domain.MaiorCategory.MajorCategoryService;
import com.infoShare.calog.domain.MinorCategory.MinorCategory;
import com.infoShare.calog.domain.MinorCategory.MinorCategoryService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public String createForm(Model model) {
        List<MajorCategory> majorCategories = majorCategoryService.findAll();
        List<MinorCategory> minorCategories = minorCategoryService.findAll();

        model.addAttribute("majorCategories", majorCategories);
        model.addAttribute("minorCategories", minorCategories);

        return "cafe_form";
    }

    @PostMapping("/create")
    public String create(@RequestParam String title,
                         @RequestParam Long majorCategoryId,
                         @RequestParam Long minorCategoryId,
                         Principal principal) {

        String email = principal.getName();
        SiteUser manager = userService.getUser(email);

        cafeService.create(title, majorCategoryId, minorCategoryId, manager);
        return "redirect:/cafes/list";
    }
}
