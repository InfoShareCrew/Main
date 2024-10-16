package com.infoShare.calog.domain.MinorCategory;

import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import com.infoShare.calog.domain.MajorCategory.MajorCategoryForm;
import com.infoShare.calog.domain.MajorCategory.MajorCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Short id){
        MinorCategory minorCategory = this.minorCategoryService.getMinorCategoryById(id);
        model.addAttribute("minorCategory", minorCategory);
        return "minorcategory_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid MinorCategoryForm minorCategoryForm, BindingResult bindingResult,
                         Principal principal, @PathVariable("id") Short id){

        if (bindingResult.hasErrors()){
            return "minorcategory_form";
        }
        MinorCategory minorCategory = this.minorCategoryService.getMinorCategoryById(id);
        if (!minorCategory.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.minorCategoryService.modifyMinorCategory(minorCategory,minorCategoryForm.getName());
        return String.format("redirect:/minorcategory/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteMinorCategory(Principal principal, @PathVariable("id") Short id) {
        MinorCategory minorCategory = this.minorCategoryService.getMinorCategoryById(id);
        if (!minorCategory.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.minorCategoryService.deleteMinorCategory(minorCategory);
        return "redirect:/";
    }
}
