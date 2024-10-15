package com.infoShare.calog.domain.neighbor;

import com.infoShare.calog.domain.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NeighborRepository extends JpaRepository<Neighbor, Long> {
    List<Neighbor> findByUserEmail(String email);
}
