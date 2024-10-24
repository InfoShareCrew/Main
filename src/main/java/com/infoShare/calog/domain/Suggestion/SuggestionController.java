package com.infoShare.calog.domain.Suggestion;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleService;
import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Cafe.CafeService;
import com.infoShare.calog.domain.Comment.CommentForm;
import com.infoShare.calog.domain.Notice.Notice;
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
@RequestMapping("suggestion")
public class SuggestionController {
    private final SuggestionService suggestionService;
    private final UserService userService;
    private final ArticleService articleService;
    private final CafeService cafeService;

    @GetMapping("/list")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Suggestion> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.suggestionService.searchSuggestions(kw, page);
        } else {
            // 기본 목록
            paging = this.suggestionService.getList(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw); // 검색어를 모델에 추가
        return "suggestion_list"; // 뷰 이름도 변경
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Long id, CommentForm commentForm,Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        model.addAttribute("suggestion", suggestion);
        model.addAttribute("article", suggestion);
        this.suggestionService.viewUp(suggestion);

        if (principal != null) {
            SiteUser user = userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname()); // 닉네임 필드가 있을 경우
        }

        return "suggestion_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(SuggestionForm suggestionForm) {
        return "suggestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid SuggestionForm suggestionForm, BindingResult bindingResult,Principal principal) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }
        SiteUser author = this.userService.getUser(principal.getName());
        this.suggestionService.createSuggestion(suggestionForm.getTitle(), suggestionForm.getContent(),author);
        return "redirect:/suggestion/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(SuggestionForm suggestionForm, @PathVariable(value = "id") Long id,Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);

        if(!suggestion.getAuthor().getEmail().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        suggestionForm.setTitle(suggestion.getTitle());
        suggestionForm.setContent(suggestion.getContent());
        return "suggestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifySuggestion(@Valid SuggestionForm suggestionForm, BindingResult bindingResult,
                                   Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (!suggestion.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.suggestionService.modifySuggestion(suggestion, suggestionForm.getTitle(), suggestionForm.getContent());
        return String.format("redirect:/suggestion/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteSuggestion(Principal principal, @PathVariable("id") Long id) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (!suggestion.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.suggestionService.delete(suggestion);
        return "redirect:/suggestion/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String suggestionVote(@PathVariable("id") Long id, Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.suggestionService.vote(suggestion, siteUser);

        Suggestion votedSuggestion = this.suggestionService.getSuggestionById(id);
        Integer count = votedSuggestion.getVoter().size();
        return count.toString();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/unvote/{id}")  //추천취소
    @ResponseBody
    public String cancelVote(@PathVariable("id") Long id, Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.suggestionService.cancelVote(suggestion, siteUser);

        Suggestion votedSuggestion = this.suggestionService.getSuggestionById(id);
        Integer count = suggestion.getVoter().size();
        return count.toString();
    }
}

