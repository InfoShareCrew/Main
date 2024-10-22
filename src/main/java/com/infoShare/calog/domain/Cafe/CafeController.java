package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleForm;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Category.CategoryService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("cafe")
public class CafeController {
    private final CafeService cafeService;
    private final UserService userService;
    private final ArticleService articleService;
    private final CategoryService categoryService;

    @GetMapping("/{cafeId}")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                        @PathVariable(value = "cafeId") Long cafeId
//                       @RequestParam(value = "page",defaultValue = "0") int page,
//                       @RequestParam(value = "kw" ,defaultValue = "") String kw
                                                                                    ) {
//        Page<Cafe> paging = this.cafeService.getList(page);
//        model.addAttribute("paging", paging);
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        model.addAttribute("cafe", cafe);
        return "cafe_index";
    }

    @GetMapping("/{cafeId}/notice")
    public String notice(Model model, @PathVariable(value = "cafeId") Long cafeId,
                       @RequestParam(value = "page",defaultValue = "0") int page,
                       @RequestParam(value = "kw" ,defaultValue = "") String kw
                                                                                ) {
        Page<Article> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.articleService.searchArticles(kw, page);
        } else {
            // 기본 목록
            paging = this.articleService.getList(page);
        }

        model.addAttribute("paging", paging);
//        List<Article> noticeList = this.articleService.getNoticeArticles(1);
//        model.addAttribute("noticeList", noticeList);
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        model.addAttribute("cafe", cafe);
        return "cafe_notice";
    }

    @GetMapping("/{cafeId}/suggest")
    public String suggest(Model model, @PathVariable(value = "cafeId") Long cafeId,
                          @RequestParam(value = "page",defaultValue = "0") int page,
                          @RequestParam(value = "kw" ,defaultValue = "") String kw
    ) {
        Page<Article> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.articleService.searchArticles(kw, page);
        } else {
            // 기본 목록
            paging = this.articleService.getList(page);
        }

        model.addAttribute("paging", paging);
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        model.addAttribute("cafe", cafe);
        return "cafe_suggest";
    }


    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Long id, CafeForm cafeForm, Principal principal) {
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
        //this.cafeService.create(cafeForm.getTitle(), cafeForm.getContent(),cafeForm.getMajorCategoryId(), cafeForm.getMinorCategoryId(), author);
        Cafe createdCafe = this.cafeService.create(cafeForm.getName(),cafeForm.getIntro(),author);
        return String.format("redirect:/cafe/%s", createdCafe.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyCafe(CafeForm cafeForm, @PathVariable("id") Long id,Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(id);
        if(!cafe.getManeger().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        // 대분류 및 소분류 카테고리 수정 로직 추가
        //MajorCategory majorCategory = majorCategoryService.findById(cafeForm.getMajorCategoryId());
       // MinorCategory minorCategory = minorCategoryService.findById(cafeForm.getMinorCategoryId());

        // 카페 정보 수정
        cafeForm.setName(cafe.getName());
        cafeForm.setIntro(cafe.getIntro());
        //cafe.setMajorCategory(majorCategory);
        //cafe.setMinorCategory(minorCategory);

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
        if (!cafe.getManeger().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }


        this.cafeService.modifyCafe(cafe,cafeForm.getName(),cafeForm.getIntro());
        return String.format("redirect:/cafe/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteCafe(Principal principal, @PathVariable("id") Long id) {
        Cafe cafe = this.cafeService.getCafeById(id);
        if (!cafe.getManeger().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.cafeService.deleteCafe(cafe);
        return String.format("redirect:/blog/%s", principal.getName());
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cafeId}/notice/create")
    public String articleCreate(ArticleForm articleForm, @PathVariable(value = "cafeId") Long cafeId) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        articleForm.setCategory(this.categoryService.getCategoryById((short) 1));
        return "article_form";
    }
}