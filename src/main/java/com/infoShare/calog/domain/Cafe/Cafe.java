package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import com.infoShare.calog.domain.MinorCategory.MinorCategory;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Cafe extends BaseEntity {
    @Column(length = 50)
    private String title;

    @ManyToOne
    private SiteUser manager;

    @ManyToOne
    private MajorCategory majorCategory;

    @ManyToOne
    private MinorCategory minorCategory;

    @ManyToOne
    private SiteUser author;
}