package com.infoShare.calog.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}
