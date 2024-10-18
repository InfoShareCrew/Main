package com.infoShare.calog.domain.user;

import com.infoShare.calog.domain.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void assignRoleToUser(String email, UserRole role) {
        SiteUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public SiteUser join(String email, String password, String nickname) {
        SiteUser siteUser = SiteUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 입력받은 비밀번호로 설정
                .nickname(nickname)
                .build();
        return userRepository.save(siteUser);
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String email, String nickname, String password) {
        Optional<SiteUser> opSiteUser = findByEmail(email);

        if (opSiteUser.isPresent()) {
            return opSiteUser.get(); // 기존 사용자 반환
        }

        // 비밀번호가 null이거나 빈 문자열인 경우 랜덤 비밀번호 생성
        String finalPassword = (password == null || password.isEmpty()) ? generateRandomPassword() : password;
        return join(email, finalPassword, nickname); // 최초 로그인 시 딱 한번 실행
    }

    // 랜덤 비밀번호 생성 메서드
    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10); // 10자리 랜덤 비밀번호
    }

    public Optional<SiteUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public SiteUser getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("siteuser not found"));
    }

    public void save(SiteUser user) {
        userRepository.save(user);
    }

    public SiteUser getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new DataNotFoundException("User not found with nickname: " + nickname));
    }
}
