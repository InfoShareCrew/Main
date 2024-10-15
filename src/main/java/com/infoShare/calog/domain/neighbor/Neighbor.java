package com.infoShare.calog.domain.neighbor;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Neighbor extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private SiteUser user;

    @ManyToOne
    @JoinColumn(name = "neighbor_id")
    @NotNull
    private SiteUser neighbor;

    // Builder를 통해 객체를 생성할 수 있도록 생성자 수정
    public Neighbor(SiteUser user, SiteUser neighbor) {
        this.user = user;
        this.neighbor = neighbor;
    }
}
