package com.infoShare.calog.domain.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    // 태그 목록 페이지네이션 및 검색 기능
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Tag> paging;

        if (kw != null && !kw.isEmpty()) {
            // 검색 기능 추가
            paging = this.tagService.searchTags(kw, page);
        } else {
            // 기본 목록
            paging = this.tagService.getList(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        List<Tag> tags = tagService.getAllTags();// 모든 태그 가져오기
        model.addAttribute("tags", tags);

        return "tag_list";
    }
}