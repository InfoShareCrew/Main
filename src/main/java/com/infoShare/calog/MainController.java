package com.infoShare.calog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping("/")
//    @GetMapping("index")
    @ResponseBody
    public String index() {
        return "홈 화면입니다.";
        // index가 홈 화면인가요 ?
    }
}