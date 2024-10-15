package com.infoShare.calog.domain.user;

import com.infoShare.calog.domain.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public SiteUser whenSocialLogin(String providerTypeCode, String email, String nickname) {
        Optional<SiteUser> opSiteUser = findByEmail(email);

        if (opSiteUser.isPresent()) return opSiteUser.get();

        // 소셜 로그인을 통한 가입시 비밀번호는 없다.
        return join(email, "", nickname); // 최초 로그인 시 딱 한번 실행
    }

    private Optional<SiteUser> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public SiteUser getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("siteuser not found"));
    }
}
