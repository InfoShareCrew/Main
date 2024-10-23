package com.infoShare.calog;

import com.infoShare.calog.domain.Cafe.Cafe;
import com.infoShare.calog.domain.Cafe.CafeService;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final CafeService cafeService;

    //    기본 홈 화면
    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        List<Cafe> cafeList = null;
        List<Cafe> myCafeList = null;
        if (principal != null) {
            cafeList = cafeService.getMyList(principal.getName());
            myCafeList = cafeService.getOwnList(principal.getName());
        }
        model.addAttribute("cafeList", cafeList);
        model.addAttribute("myCafeList", myCafeList);
        return "index";
    }
}