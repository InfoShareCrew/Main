package com.infoShare.calog.domain.MajorCategory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("majorcategory")
public class MajorCategoryController {

    private final MajorCategoryService majorCategoryService;

    @GetMapping("/list")
    public String list(Model model) {
        List<MajorCategory> majorCategories = majorCategoryService.findAll();
        model.addAttribute("majorCategories", majorCategories);
        return "major_category";
    }

    @GetMapping("/create")
    public String create (MajorCategoryForm majorCategoryForm){
        return "majorcategory_form";
    }

    @PostMapping("/create")
    public String create (@Valid MajorCategoryForm majorCategoryForm,
                          BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "majorcategory_form";
        }

        this.majorCategoryService.create(majorCategoryForm.getName());
        return "redirect:/majorcategory/list";
    }


}
