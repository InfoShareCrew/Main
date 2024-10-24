package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleForm;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.BoardCategory.BoardCategoryService;
import com.infoShare.calog.domain.Category.CategoryService;
import com.infoShare.calog.domain.Comment.CommentForm;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import com.infoShare.calog.global.Util.UtilService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashSet;

@Controller
@RequiredArgsConstructor
@RequestMapping("cafe")
public class CafeController {
    private final CafeService cafeService;
    private final UserService userService;
    private final ArticleService articleService;
    private final BoardCategoryService boardCategoryService;
    private final UtilService utilService;

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

    @GetMapping("/{cafeId}/detail/{id}")  // 카페 세부페이지
    public String detail(Model model, @PathVariable(value = "cafeId") Long cafeId, @PathVariable(value = "id") Long articleId, CommentForm commentForm, Principal principal) {
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        Article article = this.articleService.getArticleById(articleId);
        model.addAttribute("cafe", cafe);
        model.addAttribute("article", article);
        return "article_detail";
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
        Cafe createdCafe = this.cafeService.create(cafeForm.getName(),
                                                    cafeForm.getIntro(),
                                                    author
        );
        return String.format("redirect:/cafe/%s", createdCafe.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyCafe(CafeForm cafeForm,
                             @PathVariable("id") Long id,
                             Principal principal) {
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
        cafeForm.setProfileImg(cafe.getProfileImg());
        //cafe.setMajorCategory(majorCategory);
        //cafe.setMinorCategory(minorCategory);

        return "cafe_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyCafe(@Valid CafeForm cafeForm,
                             @PathVariable("id") Long id,
                             @RequestParam(value = "profile-img") MultipartFile image,
                             BindingResult bindingResult,
                             Principal principal){

        if (bindingResult.hasErrors()){
            return "cafe_form";
        }

        Cafe cafe = this.cafeService.getCafeById(id);
        if (!cafe.getManeger().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        String profileImg = null;
        if (!image.isEmpty()) {
            profileImg = this.utilService.saveImage("cafe", image);
        } else {
            profileImg = cafe.getProfileImg();
        }

        this.cafeService.modifyCafe(cafe,cafeForm.getName(),cafeForm.getIntro(), profileImg);
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
    @GetMapping("/signup/{cafeId}")
    public String cafeSignup(@PathVariable("cafeId") Long cafeId, Principal principal) {
        Cafe cafe= this.cafeService.getCafeById(cafeId);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.cafeService.signup(cafe, siteUser);

        Cafe votedCafe = this.cafeService.getCafeById(cafeId);
        return String.format("redirect:/cafe/%s", cafeId);
    }

    @GetMapping("/{cafeId}/{boardName}")
    public String boardList(Model model,
                            @PathVariable(value = "cafeId") Long cafeId,
                            @PathVariable(value = "boardName") String boardName,
                            @RequestParam(value = "page",defaultValue = "0") int page,
                            @RequestParam(value = "kw" ,defaultValue = "") String kw) {
        Page<Article> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.articleService.searchArticles(kw, page, boardName);
        } else {
            // 기본 목록
            paging = this.articleService.getList(page, boardName);
        }

        Cafe cafe = this.cafeService.getCafeById(cafeId);

        BoardCategory boardCategory = this.boardCategoryService.getCategoryByNameAndCafeId(boardName, cafeId);

        model.addAttribute("paging", paging);
        model.addAttribute("cafe", cafe);
        model.addAttribute("boardCategory", boardCategory);
        return "article_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cafeId}/{boardName}/create")
    public String articleCreate(Model model,
                                ArticleForm articleForm,
                                @PathVariable(value = "cafeId") Long cafeId,
                                @PathVariable(value = "boardName") String boardName,
                                Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);

        if ((!cafe.getManeger().getEmail().equals(principal.getName()) && boardName.equals("공지사항"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        articleForm.setBoardName(boardName);
        model.addAttribute("cafe", cafe);
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{cafeId}/{boardName}/create")
    public String articleCreate(Model model,
                               @Valid ArticleForm articleForm,
                               @PathVariable(value = "cafeId") Long cafeId,
                               @PathVariable(value = "boardName") String boardName,
                               BindingResult bindingResult,
                               Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("cafe", cafe);
            return "article_form";
        }

        this.articleService.createArticle(
                articleForm.getTitle(),
                articleForm.getContent(),
                this.userService.findByEmail(principal.getName()),
                this.boardCategoryService.getCategoryByName(boardName),
                this.cafeService.getCafeById(cafeId),
                articleForm.getTags()
        );
        return String.format("redirect:/cafe/%s/%s", cafeId, boardName);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cafeId}/{boardName}/delete/{articleId}")
    public String articleDelete(@PathVariable(value = "cafeId") Long cafeId,
                                @PathVariable(value = "boardName") String boardName,
                                @PathVariable(value = "articleId") Long articleId,
                                Principal principal) {
        Article article = this.articleService.getArticleById(articleId);
        if (!article.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.articleService.delete(article);
        return String.format("redirect:/cafe/%s/%s", cafeId, boardName);
    }
}