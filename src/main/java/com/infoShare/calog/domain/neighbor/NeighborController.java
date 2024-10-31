package com.infoShare.calog.domain.neighbor;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/neighbors")
public class NeighborController {
    private final UserService userService;
    private final NeighborService neighborService;

    // 현재 사용자의 이웃 목록 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String getNeighbors(Model model, Principal principal) {
        Set<SiteUser> neighbors = userService.getUser(principal.getName()).getNeighbor(); // 이웃 목록 가져오기
        model.addAttribute("neighborList", new ArrayList<>(neighbors));
        return "neighbors"; // 이웃 목록을 보여주는 템플릿
    }

    // 이웃 추가
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{neighborEmail}")
    public String addNeighbor(@PathVariable(value = "neighborEmail") String neighborEmail,
                                                Principal principal) {
        neighborService.addNeighbor(userService.getUser(principal.getName()),
                                   userService.getUser(neighborEmail));
        return String.format("redirect:/blog/%s", neighborEmail);
    }

}
