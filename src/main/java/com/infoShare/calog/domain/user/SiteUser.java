package com.infoShare.calog.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
