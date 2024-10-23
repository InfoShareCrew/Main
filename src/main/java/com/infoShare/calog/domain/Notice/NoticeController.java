package com.infoShare.calog.domain.Notice;

import com.infoShare.calog.domain.Comment.Comment;
import com.infoShare.calog.domain.Comment.CommentForm;
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
@RequestMapping("notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model,
                       @ModelAttribute("basedEntity") BaseEntity baseEntity,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Notice> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.noticeService.searchNotices(kw, page);
        } else {
            // 기본 목록
            paging = this.noticeService.getList(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw); // 검색어를 모델에 추가
        return "notice_list";
    }


    @GetMapping("/create")
    public String create(NoticeForm noticeForm, Model model) {
        return "notice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        SiteUser author = this.userService.getUser(principal.getName());
        this.noticeService.create(noticeForm.getTitle(), noticeForm.getContent(), author);
        return "redirect:/notice/list";
    }


    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable(value = "id") Long id, CommentForm commentForm, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        this.noticeService.viewUp(notice);

        if (principal != null) {
            SiteUser user = userService.getUser(principal.getName());
            model.addAttribute("userNickname", user.getNickname());
        }

        return "notice_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(NoticeForm noticeForm, @PathVariable("id") Long id, Model model, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        if (!notice.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        noticeForm.setTitle(notice.getTitle());
        noticeForm.setContent(notice.getContent());
        model.addAttribute("noticeForm", noticeForm);
        return "notice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid NoticeForm noticeForm, BindingResult bindingResult,
                               Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        Notice notice = this.noticeService.getNoticeById(id);
        if (!notice.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeService.modify(notice, noticeForm.getTitle(), noticeForm.getContent()); // 수정 메서드 호출
        return String.format("redirect:/notice/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") Long id) {
        Notice notice = this.noticeService.getNoticeById(id);
        if (!notice.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeService.delete(notice);
        return "redirect:/notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    @ResponseBody
    public String vote(@PathVariable("id") Long id, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.noticeService.vote(notice, siteUser);

        Notice votedNotice = this.noticeService.getNoticeById(id);
        Integer count = votedNotice.getVoter().size();
        return count.toString();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/unvote/{id}")  //추천취소
    @ResponseBody
    public String cancelVote(@PathVariable("id") Long id, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.noticeService.cancelVote(notice, siteUser);

        Notice votedNotice = this.noticeService.getNoticeById(id);
        Integer count = notice.getVoter().size();
        return count.toString();
    }
}
