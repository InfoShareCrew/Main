package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.Category.CategoryService;
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
@RequestMapping("cafe")
public class CafeController {
    private final CafeService cafeService;
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Cafe> paging;

        if (kw != null && !kw.isEmpty()) {
            paging = this.cafeService.searchCafes(kw, page);
        } else {
            paging = this.cafeService.getList(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "cafe_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Long id, CafeForm cafeForm, Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(id);
        model.addAttribute("cafe", cafe);

        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }

        return "cafe_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(CafeForm cafeForm) {
        return "cafe_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid CafeForm cafeForm, BindingResult bindingResult,Principal principal) {

        if (bindingResult.hasErrors()) {
            return "cafe_form";
        }

        SiteUser author = this.userService.getUser(principal.getName());

        this.cafeService.create(cafeForm.getTitle(),cafeForm.getContent(),author);
        return "redirect:/cafe/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyCafe(CafeForm cafeForm, Model model,@PathVariable("id") Long id,Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(id);
        if(!cafe.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        cafeForm.setTitle(cafe.getTitle());
        cafeForm.setContent(cafe.getContent());
        model.addAttribute("cafeForm",cafeForm);
        return "cafe_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyCafe(@Valid CafeForm cafeForm, BindingResult bindingResult,
                             Principal principal,@PathVariable("id") Long id){

        if (bindingResult.hasErrors()){
            return "cafe_form";
        }
        Cafe cafe = this.cafeService.getCafeById(id);
        if (!cafe.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }


        this.cafeService.modifyCafe(cafe,cafeForm.getTitle(),cafeForm.getContent());
        return String.format("redirect:/cafe/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteCafe(Principal principal, @PathVariable("id") Long id) {
        Cafe cafe = this.cafeService.getCafeById(id);
        if (!cafe.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.cafeService.deleteCafe(cafe);
        return "redirect:/cafe/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String cafeVote(@PathVariable("id") Long id, Principal principal) {
        Cafe cafe= this.cafeService.getCafeById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.cafeService.vote(cafe, siteUser);

        Cafe votedCafe = this.cafeService.getCafeById(id);
        Integer count = votedCafe.getVoter().size();
        return count.toString();
    }
}