package com.infoShare.calog.domain.neighbor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/neighbors")
public class NeighborController {
    private final NeighborService neighborService;

    // 이웃 추가
    @PostMapping("/add/{neighborUserId}")
    public ResponseEntity<Neighbor> addNeighbor(@PathVariable Long neighborUserId) {
        Neighbor newNeighbor = neighborService.addNeighbor(neighborUserId);
        return ResponseEntity.status(201).body(newNeighbor);
    }

    // 현재 사용자의 이웃 목록 조회
    @GetMapping
    public String getNeighbors(Model model, Principal principal) {
        if (principal == null) {
            // 사용자가 로그인하지 않은 경우
            return "redirect:/user/login"; // 로그인 페이지로 리디렉션
        }

        String email = principal.getName(); // 로그인한 사용자의 이메일
        List<Neighbor> neighbors = neighborService.getNeighborsByUserEmail(email); // 이웃 목록 가져오기
        model.addAttribute("neighbors", neighbors);
        return "neighbors"; // 이웃 목록을 보여주는 템플릿
    }
}
