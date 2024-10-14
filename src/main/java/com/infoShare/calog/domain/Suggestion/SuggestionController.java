package com.infoShare.calog.domain.Suggestion;

import com.infoShare.calog.domain.Comment.CommentForm;
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
@RequestMapping("suggestion")
public class SuggestionController {

    private final SuggestionService suggestionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Suggestion> paging = this.suggestionService.getList(page);
        model.addAttribute("paging", paging);
        return "suggestion_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Integer id, CommentForm commentForm) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        model.addAttribute("suggestion", suggestion);
        this.suggestionService.viewUp(suggestion);
        return "suggestion_detail";
    }

    @GetMapping("/create")
    public String create(SuggestionForm suggestionForm) {
        return "suggestion_form";
    }

    @PostMapping("/create")
    public String create(@Valid SuggestionForm suggestionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }
        this.suggestionService.createSuggestion(suggestionForm.getTitle(), suggestionForm.getContent());
        return "redirect:/suggestion/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable(value = "id") Integer id) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        model.addAttribute("suggestion", suggestion);
        return "suggestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifySuggestion(@Valid SuggestionForm suggestionForm, BindingResult bindingResult,
                                   Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "suggestion_form";
        }
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (!suggestion.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.suggestionService.modifySuggestion(suggestion, suggestionForm.getTitle(), suggestionForm.getContent());
        return String.format("redirect:/suggestion/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteSuggestion(Principal principal, @PathVariable("id") Integer id) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        if (!suggestion.getAuthor().getNickname().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.suggestionService.delete(suggestion);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String suggestionVote(@PathVariable("id") Integer id, Principal principal) {
        Suggestion suggestion = this.suggestionService.getSuggestionById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.suggestionService.vote(suggestion, siteUser);
        Suggestion votedSuggestion = this.suggestionService.getSuggestionById(id);
        Integer count = votedSuggestion.getVoter().size();
        return count.toString();
    }
}
