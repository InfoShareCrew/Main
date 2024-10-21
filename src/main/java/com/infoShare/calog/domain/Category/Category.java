package com.infoShare.calog.domain.Category;

import com.infoShare.calog.domain.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(length = 50)
    private String majorCategory;

    @Column(length = 50)
    private String minorCategory;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @ManyToOne
    private SiteUser author;
}