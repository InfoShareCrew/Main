package com.infoShare.calog.domain.user;

import com.infoShare.calog.global.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "siteuser_roles", joinColumns = @JoinColumn(name = "siteuser_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    public void updateBlog(String intro, String address, String nickname) {
        this.intro = intro;
        this.address = address;
        this.nickname = nickname;
    }
}
