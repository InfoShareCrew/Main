package com.infoShare.calog.global.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String email, String password, String nickname) {
        SiteUser user = new SiteUser();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        this.userRepository.save(user);
        return user;
    }

    @Transactional
    public SiteUser processKakaoUser(String email, String nickname) {
        // 사용자 조회
        SiteUser user = userRepository.findByEmail(email).get();

        // 사용자가 존재하지 않으면 새 사용자 생성
        if (user == null) {
            user = new SiteUser();
            user.setEmail(email);
            user.setNickname(nickname);
            userRepository.save(user);
        } else {
            // 이미 존재하는 사용자 정보 업데이트
            user.setNickname(nickname);
            userRepository.save(user);
        }

        return user;
    }
}
