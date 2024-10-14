package com.infoShare.calog.domain.user;

import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SiteUser extends BaseEntity {
    @Column(unique = true)
    @Email
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String intro;

    @Column(length = 2000)
    private String address;
}
