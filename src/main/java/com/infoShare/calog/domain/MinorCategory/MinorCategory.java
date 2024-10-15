package com.infoShare.calog.domain.MinorCategory;
import com.infoShare.calog.domain.MaiorCategory.MajorCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class MinorCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @ManyToOne
    private MajorCategory majorCategory;
}
