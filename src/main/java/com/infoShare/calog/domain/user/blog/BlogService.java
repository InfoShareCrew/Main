package com.infoShare.calog.domain.user.blog;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final UserService userService;

    public SiteUser getUserBlog(String email) {
        return userService.getUser(email);
    }

    public void updateUserBlog(SiteUser siteUser, String intro, String address, String nickname) {
        siteUser.updateBlog(intro, address, nickname);
        userService.save(siteUser); // 사용자 정보를 업데이트
    }
}
