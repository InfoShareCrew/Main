package com.infoShare.calog.domain.user.blog;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final UserService userService;

    public SiteUser getUserProfile(String email) {
        return userService.getUser(email);
    }

    public void updateUserProfile(SiteUser siteUser, String intro, String address, String nickname, String profileImg) {
        siteUser.updateBlog(intro, address, nickname, profileImg);
        userService.save(siteUser); // 사용자 정보를 업데이트
    }
}
