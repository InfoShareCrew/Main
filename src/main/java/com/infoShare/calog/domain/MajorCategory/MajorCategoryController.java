package com.infoShare.calog.domain.MajorCategory;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Cafe.CafeForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Short id){
        MajorCategory majorCategory = this.majorCategoryService.getMajorCategoryById(id);
        model.addAttribute("majorCategory", majorCategory);
        return "majorcategory_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid MajorCategoryForm majorCategoryForm, BindingResult bindingResult,
                         Principal principal, @PathVariable("id") Short id){

        if (bindingResult.hasErrors()){
            return "majorcategory_form";
        }
        MajorCategory majorCategory= this.majorCategoryService.getMajorCategoryById(id);
        if (!majorCategory.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.majorCategoryService.modifyMajorCategory(majorCategory,majorCategoryForm.getName());
        return String.format("redirect:/majorcategory/detail/%s",id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteMajorCategory(Principal principal, @PathVariable("id") Short id) {
        MajorCategory majorCategory = this.majorCategoryService.getMajorCategoryById(id);
        if (!majorCategory.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.majorCategoryService.deleteMajorCategory(majorCategory);
        return "redirect:/";
    }

}
