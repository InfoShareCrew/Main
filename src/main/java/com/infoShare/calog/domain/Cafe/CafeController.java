package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleForm;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.BoardCategory.BoardCategoryService;
import com.infoShare.calog.domain.Category.Category;
import com.infoShare.calog.domain.Category.CategoryService;
import com.infoShare.calog.domain.Comment.CommentForm;
import com.infoShare.calog.domain.Tag.Tag;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("cafe")
public class CafeController {
    private final CafeService cafeService;
    private final UserService userService;
    private final ArticleService articleService;
    private final BoardCategoryService boardCategoryService;
    private final UtilService utilService;
    private final CategoryService categoryService;

    @GetMapping("/{cafeId}")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                        @PathVariable(value = "cafeId") Long cafeId,Principal principal
                                                                                    ) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        model.addAttribute("cafe", cafe);

        // 인기글 5개 가져오기
        List<Article> popularArticles = this.articleService.getPopularArticlesForCafe(5, cafeId);
        model.addAttribute("popularArticles", popularArticles);

        return "cafe_index";
    }

    @GetMapping("/{cafeId}/detail/{id}")  // 카페 세부페이지
    public String detail(Model model, @PathVariable(value = "cafeId") Long cafeId, @PathVariable(value = "id") Long articleId, CommentForm commentForm, Principal principal) {
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }
        this.articleService.incrementViews(articleId);

        Cafe cafe = this.cafeService.getCafeById(cafeId);
        Article article = this.articleService.getArticleById(articleId);
        this.articleService.viewUp(article);
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
    @GetMapping("/modify/{cafeId}")
    public String modifyCafe(CafeForm cafeForm,
                             @PathVariable("cafeId") Long cafeId,
                             Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        if(!cafe.getManager().getEmail().equals(principal.getName())){
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
    @PostMapping("/modify/{cafeId}")
    public String modifyCafe(@Valid CafeForm cafeForm,
                             @PathVariable("cafeId") Long cafeId,
                             @RequestParam(value = "profile-img") MultipartFile image,
                             BindingResult bindingResult,
                             Principal principal){

        if (bindingResult.hasErrors()){
            return "cafe_form";
        }

        Cafe cafe = this.cafeService.getCafeById(cafeId);
        if (!cafe.getManager().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        String profileImg = null;
        if (!image.isEmpty()) {
            profileImg = this.utilService.saveImage("cafe", image);
        } else {
            profileImg = cafe.getProfileImg();
        }

        this.cafeService.modifyCafe(cafe,cafeForm.getName(),cafeForm.getIntro(), profileImg);
        return String.format("redirect:/cafe/%s", cafeId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{cafeId}")
    public String deleteCafe(Principal principal, @PathVariable("cafeId") Long cafeId) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        if (!cafe.getManager().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.cafeService.deleteCafe(cafe);
        return String.format("redirect:/blog/%s", principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{cafeId}")
    @ResponseBody
    public String cafeVote(@PathVariable("cafeId") Long cafeId, Principal principal) {
        Cafe cafe= this.cafeService.getCafeById(cafeId);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.cafeService.vote(cafe, siteUser);

        Cafe votedCafe = this.cafeService.getCafeById(cafeId);
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
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "kw", defaultValue = "") String kw,
                            @RequestParam(value = "tag", required = false) String tag) {
        Page<Article> paging;

        if (kw != null && !kw.isEmpty()) {
            paging = this.articleService.searchArticles(kw, page, boardName, cafeId);
        } else if (tag != null && !tag.isEmpty()) {
            paging = this.articleService.searchArticlesByTag(tag, page, boardName);
        } else {
            paging = this.articleService.getList(page, boardName, cafeId);
        }

        Cafe cafe = this.cafeService.getCafeById(cafeId);
        BoardCategory boardCategory = this.boardCategoryService.getCategoryByNameAndCafeId(boardName, cafeId);

        model.addAttribute("paging", paging);
        model.addAttribute("cafe", cafe);
        model.addAttribute("boardCategory", boardCategory);
        model.addAttribute("tag", tag);  // 태그 검색기능
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

        if ((!cafe.getManager().getEmail().equals(principal.getName()) && boardName.equals("공지사항"))) {
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
                this.boardCategoryService.getCategoryByNameAndCafeId(boardName, cafeId),
                this.cafeService.getCafeById(cafeId),
                articleForm.getTags()
        );
        return String.format("redirect:/cafe/%s/%s", cafeId, boardName);
    }

    @GetMapping("/{cafeId}/{boardName}/modify/{articleId}")
    public String modifyArticle(Model model,
                                ArticleForm articleForm,
                                @PathVariable(value = "cafeId") Long cafeId,
                                @PathVariable(value = "boardName") String boardName,
                                @PathVariable(value = "articleId") Long articleId,
                                Principal principal) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);

        if ((!cafe.getManager().getEmail().equals(principal.getName()) && boardName.equals("공지사항"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        Article article = this.articleService.getArticleById(articleId);
        String tagString = article.getTags().stream()
                .map(Tag::getName) // Tag 객체에서 이름을 가져옵니다.
                .collect(Collectors.joining(", "));

        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());
        articleForm.setBoardName(article.getBoardCategory().getName());
        articleForm.setTags(tagString);
        articleForm.setBoardName(boardName);

        model.addAttribute("cafe", cafe);

        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{cafeId}/{boardName}/modify/{articleId}")
    public String modifyArticle(Model model,
                                @Valid ArticleForm articleForm,
                                @PathVariable(value = "cafeId") Long cafeId,
                                @PathVariable(value = "boardName") String boardName,
                                @PathVariable(value = "articleId") Long articleId,
                                BindingResult bindingResult,
                                Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories()); // 카테고리가져오기
            return "article_form";
        }
        Article article = this.articleService.getArticleById(articleId);

        this.articleService.modify(article,
                                    articleForm.getTitle(),
                                    articleForm.getContent(),
                                    this.boardCategoryService.getCategoryByName(articleForm.getBoardName()),
                                    articleForm.getTags());
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

    @GetMapping("/search")
    public String searchCafe(@RequestParam("kw") String keyword, Model model) {
        List<Cafe> cafes = cafeService.findByName(keyword); // 키워드로 카페 검색

        if (!cafes.isEmpty()) {
            model.addAttribute("cafes", cafes);
            return "index"; // 검색 결과를 보여줄 페이지
        } else {
            model.addAttribute("error", "카페를 찾을 수 없습니다.");
            return "index"; // 검색 실패 시 보여줄 페이지
        }
    }
}