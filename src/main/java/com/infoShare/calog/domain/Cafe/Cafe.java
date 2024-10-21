package com.infoShare.calog.domain.Cafe;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
public class Cafe extends BaseEntity {
    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

}