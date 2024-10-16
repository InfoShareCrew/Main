package com.infoShare.calog.domain.MinorCategory;
import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class MinorCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(length = 50)
    private String name;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private MajorCategory majorCategory;

    @ManyToOne
    private SiteUser author;
}
