package com.infoShare.calog.domain.Suggestion.controller;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Cafe.CafeService;
import com.infoShare.calog.domain.Comment.CommentForm;
import com.infoShare.calog.domain.Suggestion.SuggestionForm;
import com.infoShare.calog.domain.Suggestion.service.SuggestionCommentService;
import com.infoShare.calog.domain.Suggestion.service.SuggestionService;
import com.infoShare.calog.domain.Suggestion.entity.Suggestion;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
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
@RequestMapping("/cafe/{cafeId}/suggestion")
public class SuggestionController {
    private final SuggestionService suggestionService;
    private final SuggestionCommentService suggestionCommentService;
    private final UserService userService;
    private final CafeService cafeService;

    @GetMapping("")
    public String list(Model model,
                       @PathVariable(value = "cafeId") Long cafeId,
                       @RequestParam(value = "page",defaultValue = "0") int page,
                       @RequestParam(value = "kw" ,defaultValue = "") String kw) {
        Page<Suggestion> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.suggestionService.searchSuggestions(kw, page, cafeId);
        } else {
            // 기본 목록
            paging = this.suggestionService.getList(page, cafeId);
        }

        Cafe cafe = this.cafeService.getCafeById(cafeId);

        model.addAttribute("paging", paging);
        model.addAttribute("suggestionCommentList", this.suggestionCommentService.list());
        model.addAttribute("cafe", cafe);
        return "suggestion_list";
    }


    @GetMapping("/create")
    public String create(Model model,
                         SuggestionForm suggestionForm,
                         @PathVariable("cafeId") Long cafeId) {
        Cafe cafe = this.cafeService.getCafeById(cafeId);
        model.addAttribute("cafe", cafe);
        return "suggestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid SuggestionForm suggestionForm,
                         @PathVariable("cafeId") Long cafeId,
                         BindingResult bindingResult,
                         Principal principal) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }

        Cafe cafe = cafeService.getCafeById(cafeId);
        SiteUser user = userService.getUser(principal.getName());

        if (!user.getCafe().contains(cafe)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "생성권한이 없습니다.");
        }

        this.suggestionService.create(suggestionForm.getTitle(),
                                        suggestionForm.getContent(),
                                        this.userService.findByEmail(principal.getName()),
                                        cafe);
        return String.format("redirect:/cafe/%s/suggestion", cafeId);
    }


    @GetMapping("/detail/{id}")
    public String detail(Model model,
                         CommentForm commentForm,
                         @PathVariable(value = "id") Long id,
                         @PathVariable(value = "cafeId") Long cafeId,
                         Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        this.suggestionService.viewUp(suggestion);

        if (principal != null) {
            SiteUser user = userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }

        Cafe cafe = this.cafeService.getCafeById(cafeId);

        model.addAttribute("suggestion", suggestion);
        model.addAttribute("cafe", cafe);
        return "suggestion_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Model model,
                         SuggestionForm suggestionForm,
                         @PathVariable("id") Long id,
                         @PathVariable(value = "cafeId") Long cafeId,
                         Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);

        if (!suggestion.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        Cafe cafe = this.cafeService.getCafeById(cafeId);

        suggestionForm.setTitle(suggestion.getTitle());
        suggestionForm.setContent(suggestion.getContent());

        model.addAttribute("suggestionForm", suggestionForm);
        model.addAttribute("cafe", cafe);
        return "suggestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid SuggestionForm suggestionForm,
                         @PathVariable("id") Long id,
                         @PathVariable("cafeId") Long cafeId,
                         BindingResult bindingResult,
                         Principal principal) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (!suggestion.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.suggestionService.modify(suggestion, suggestionForm.getTitle(), suggestionForm.getContent()); // 수정 메서드 호출
        return String.format("redirect:/cafe/%s/suggestion/detail/%s", cafeId, id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         @PathVariable("cafeId") Long cafeId,
                         Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (!suggestion.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.suggestionService.delete(suggestion);
        return String.format("redirect:/cafe/%s/suggestion", cafeId);
    }
}