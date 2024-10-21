package com.infoShare.calog.domain.Category;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Category> paging;

        if (kw != null && !kw.isEmpty()) {
            paging = this.categoryService.searchCategories(kw, page);
        } else {
            paging = this.categoryService.getList(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "category_list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(CategoryForm categoryForm) {
        return "category_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid CategoryForm categoryForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "category_form";
        }
        SiteUser author = this.userService.getUser(principal.getName());
        this.categoryService.create(categoryForm.getMajorCategory(),categoryForm.getMinorCategory(), author);
        return "redirect:/category/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Short id, CategoryForm categoryForm, Principal principal) {
        Category category = this.categoryService.getCategoryById(id);
        model.addAttribute("category", category);

        if (principal != null) {
            SiteUser user = userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }

        return "category_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(CategoryForm categoryForm, @PathVariable(value = "id") Short id, Principal principal) {
        Category category = this.categoryService.getCategoryById(id);

        if(!category.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        categoryForm.setMajorCategory(category.getMajorCategory());
        categoryForm.setMinorCategory(category.getMinorCategory());
        return "category_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid CategoryForm categoryForm, BindingResult bindingResult,
                         Principal principal, @PathVariable("id") Short id) {
        if (bindingResult.hasErrors()) {
            return "category_form";
        }
        Category category = this.categoryService.getCategoryById(id);
        if (!category.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.categoryService.modify(category, categoryForm.getMajorCategory(), categoryForm.getMinorCategory());
        return String.format("redirect:/category/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") Short id) {
        Category category = this.categoryService.getCategoryById(id);
        if (!category.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.categoryService.delete(category);
        return "redirect:/category/list";
    }
}