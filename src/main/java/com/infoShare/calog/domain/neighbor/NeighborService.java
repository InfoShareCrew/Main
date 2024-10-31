package com.infoShare.calog.domain.neighbor;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NeighborService {
    private final UserRepository userRepository;

    public SiteUser addNeighbor(SiteUser user, SiteUser neighbor) {
        user.getNeighbor().add(neighbor);
        return userRepository.save(user);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof SiteUser) {
            SiteUser user = (SiteUser) principal;
            return user.getId(); // 또는 필요한 사용자 ID 반환
        } else {
            // 사용자 정보가 SiteUser가 아닐 경우 처리
            throw new IllegalArgumentException("Current user is not a SiteUser");
        }
    }
}
