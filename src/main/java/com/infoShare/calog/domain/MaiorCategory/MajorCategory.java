package com.infoShare.calog.domain.MaiorCategory;

import com.infoShare.calog.domain.MinorCategory.MinorCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class MajorCategory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;
}