package com.infoShare.calog.domain.neighbor;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeighborService {
    private final NeighborRepository neighborRepository;
    private final UserRepository userRepository;

    public NeighborService(NeighborRepository neighborRepository, UserRepository userRepository) {
        this.neighborRepository = neighborRepository;
        this.userRepository = userRepository;
    }

    public Neighbor addNeighbor(Long neighborUserId) {
        Long userId = getCurrentUserId();
        SiteUser siteUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        SiteUser neighbor = userRepository.findById(neighborUserId)
                .orElseThrow(() -> new RuntimeException("Neighbor not found"));

        Neighbor newNeighbor = new Neighbor(siteUser, neighbor);
        return neighborRepository.save(newNeighbor);
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

    public List<Neighbor> getNeighborsByUserEmail(String email) {
        // 사용자 이메일에 해당하는 이웃 목록을 반환
        return neighborRepository.findByUserEmail(email);
    }
}
