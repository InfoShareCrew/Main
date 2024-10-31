package com.infoShare.calog.domain.Suggestion.controller;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Cafe.CafeService;
import com.infoShare.calog.domain.Suggestion.entity.SuggestionComment;
import com.infoShare.calog.domain.Suggestion.service.SuggestionCommentService;
import com.infoShare.calog.domain.Suggestion.SuggestionForm;
import com.infoShare.calog.domain.Suggestion.entity.Suggestion;
import com.infoShare.calog.domain.Suggestion.service.SuggestionService;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/cafe/{cafeId}/suggestion/{suggestId}/comment")
public class SuggestionCommentController {
    private final SuggestionService suggestionService;
    private final SuggestionCommentService suggestionCommentService;
    private final CafeService cafeService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String createManegeComment(Model model,
                                      SuggestionForm suggestionForm,
                                      @PathVariable("cafeId") Long cafeId,
                                      @PathVariable("suggestId") Long suggestId,
                                      Principal principal) {

        Cafe cafe = cafeService.getCafeById(cafeId);
        Suggestion suggestion = suggestionService.getSuggestionById(suggestId);

        if (!cafe.getManager().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "생성권한이 없습니다.");
        }

        suggestionForm.setTitle("Re: "+suggestion.getTitle());
        suggestionForm.setSuggestionId(suggestion.getId());
        model.addAttribute("cafe", cafe);

        return "suggestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public String createManegeComment(@Valid SuggestionForm suggestionForm,
                                      @PathVariable("cafeId") Long cafeId,
                                      @PathVariable("suggestId") Long suggestId,
                                      BindingResult bindingResult,
                                      Principal principal) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }

        Cafe cafe = cafeService.getCafeById(cafeId);
        SiteUser user = userService.getUser(principal.getName());

        this.suggestionCommentService.create(suggestId,
                                            suggestionForm.getTitle(),
                                            suggestionForm.getContent(),
                                            user,
                                            cafe);

        return String.format("redirect:/cafe/%s/suggestion", cafeId);
    }

    @GetMapping("/detail/{commentId}")
    public String createManegeComment(Model model,
                                      @PathVariable("cafeId") Long cafeId,
                                      @PathVariable("suggestId") Long suggestId,
                                      @PathVariable("commentId") Long commentId) {

        Cafe cafe = cafeService.getCafeById(cafeId);
        SuggestionComment comment = suggestionCommentService.getComment(commentId);

        model.addAttribute("cafe", cafe);
        model.addAttribute("comment", comment);

        return "suggestion_comment_detail";
    }
}
