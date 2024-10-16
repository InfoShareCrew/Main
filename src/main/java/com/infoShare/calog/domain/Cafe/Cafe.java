package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import com.infoShare.calog.domain.MinorCategory.MinorCategory;
import com.infoShare.calog.domain.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @ManyToOne
    private SiteUser manager;

    @ManyToOne
    private MajorCategory majorCategory;

    @ManyToOne
    private MinorCategory minorCategory;
}
