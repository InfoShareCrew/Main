package com.infoShare.calog.domain.user;

import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
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

    private String profileImg;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "siteuser_roles", joinColumns = @JoinColumn(name = "siteuser_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    public void updateBlog(String intro, String address, String nickname, String profileImg) {
        this.intro = intro;
        this.address = address;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
