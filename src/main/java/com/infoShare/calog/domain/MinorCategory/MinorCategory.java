package com.infoShare.calog.domain.MinorCategory;
import com.infoShare.calog.domain.MajorCategory.MajorCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class MinorCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(length = 50)
    private String name;

    @ManyToOne
    private MajorCategory majorCategory;
}
