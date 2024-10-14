package com.infoShare.calog.global.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private BaseEntity(Builder builder) {
        this.id  = builder().id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }



    public static class Builder {
        private Long id;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate; // 수정
            return this;
        }

        public BaseEntity build() {
            return new BaseEntity(this);
        }
    }

}
