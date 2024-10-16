package com.infoShare.calog.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

class CustomOAuth2User extends User implements OAuth2User {
    private final Map<String, Object> attributes;

    public CustomOAuth2User(String email, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(email, password, authorities);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}


//package com.infoShare.calog.global.security;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.Map;
//class CustomOAuth2User extends User implements OAuth2User {
//    public CustomOAuth2User(String email, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(email, password, authorities);
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return null;
//    }
//
//    @Override
//    public String getName() {
//        return getUsername();
//    }
//}
