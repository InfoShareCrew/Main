package com.infoShare.calog.domain.BoardType;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BoardType extends BaseEntity {
    @Column(length = 200)
    private String name;

    @Column(length = 1)
    private String enable_grade;

    @ManyToOne
    private BoardCategory boardCategory;

    // TODO: 카페, 사용자 넣기
}
