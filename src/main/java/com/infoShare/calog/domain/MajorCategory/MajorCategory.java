package com.infoShare.calog.domain.MajorCategory;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MajorCategory  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Column(length = 50)
    private String name;

    @ManyToOne
    private SiteUser author;
}