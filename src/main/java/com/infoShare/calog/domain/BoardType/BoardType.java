package com.infoShare.calog.domain.BoardType;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class BoardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String name;

    @Column(length = 1)
    private String enable_grade;

    private LocalDateTime createDate;

    @ManyToOne
    private BoardCategory boardCategory;

    // TODO: 카페, 사용자 넣기
}
