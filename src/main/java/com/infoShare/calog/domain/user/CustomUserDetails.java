package com.infoShare.calog.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final SiteUser siteUser;

    public CustomUserDetails(SiteUser siteUser) {
        this.siteUser = siteUser;
    }

    @Override
    public String getUsername() {
        return siteUser.getNickname(); // 또는 email로 변경 가능
    }

    @Override
    public String getPassword() {
        return siteUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return siteUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 조건에 따라 수정 가능
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 조건에 따라 수정 가능
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 조건에 따라 수정 가능
    }

    @Override
    public boolean isEnabled() {
        return true; // 조건에 따라 수정 가능
    }

    public String getNickname() {
        return siteUser.getNickname(); // nickname 접근 메서드
    }
}
