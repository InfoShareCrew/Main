package com.infoShare.calog.domain.MinorCategory;

import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import com.infoShare.calog.domain.MajorCategory.MajorCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("minorcategory")
public class MinorCategoryController {

    private final MinorCategoryService minorCategoryService;
    private final MajorCategoryService majorCategoryService;

    @GetMapping("/list")
    public String list(Model model) {
        List<MinorCategory> minorCategories = minorCategoryService.findAll();
        model.addAttribute("minorCategories", minorCategories);
        return "minor_category";
    }

    @GetMapping("/create")
    public String create (MinorCategoryForm minorCategoryForm, Model model){
        List<MajorCategory> majorCategories = majorCategoryService.findAll();
        model.addAttribute("majorCategories", majorCategories);

        return "minorcategory_form";
    }

    @PostMapping("/create")
    public String create (@Valid MinorCategoryForm minorCategoryForm,
                          BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "minorcategory_form";
        }

        Short majorCategoryId = minorCategoryForm.getMajorCategoryId();
        MajorCategory majorCategory = majorCategoryService.findById(majorCategoryId);

        this.minorCategoryService.create(minorCategoryForm.getName(), majorCategory);
        return "redirect:/minorcategory/list";
    }
}
