package com.infoShare.calog.domain.BoardCategory;

import lombok.Getter;

@Getter
public enum CategoryList {
    Category1(""),
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    private final String value;

    CategoryList(String value) {
        this.value = value;
    }
}
