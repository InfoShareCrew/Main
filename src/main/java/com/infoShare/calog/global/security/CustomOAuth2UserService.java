package com.infoShare.calog.global.security;

import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String oauthId;
        String email;
        String nickname;

        if ("GOOGLE".equals(providerTypeCode)) {
            oauthId = oAuth2User.getAttribute("sub"); // 구글의 고유 ID
            email = oAuth2User.getAttribute("email"); // 구글 이메일
            nickname = oAuth2User.getAttribute("name"); // 구글 사용자 이름
        } else if ("KAKAO".equals(providerTypeCode)) {
            oauthId = oAuth2User.getName(); // 카카오 ID
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map attributesProperties = (Map) attributes.get("properties");
            nickname = (String) attributesProperties.get("nickname");
            email = providerTypeCode + "__@%s".formatted(oauthId); // 카카오 이메일 처리 (예시)
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider"), "Invalid OAuth2 provider");
        }

        // 비밀번호 처리: Google의 경우 비밀번호는 빈 문자열이므로 랜덤 비밀번호 생성
        String password = providerTypeCode.equals("GOOGLE") ? "" : null;

        // 사용자 정보를 데이터베이스에 저장하거나 업데이트
        SiteUser siteUser = userService.whenSocialLogin(providerTypeCode, email, nickname, password);
        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new CustomOAuth2User(siteUser.getEmail(), siteUser.getPassword(), authorityList, oAuth2User.getAttributes());
    }
}
