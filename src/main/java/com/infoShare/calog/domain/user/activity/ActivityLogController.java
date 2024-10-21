package com.infoShare.calog.domain.user.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/{userId}")
    public String getActivityLogs(@PathVariable Long userId, Model model) {
        List<Map<String, Object>> logs = activityLogService.getActivityLogsByUserId(userId);
        model.addAttribute("logs", logs);
        return "blog_view"; // 해당 HTML 파일의 이름
    }
}