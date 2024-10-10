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

    @Transactional
    public void processGoogleUser(String email, String nickname) {
        // 사용자 정보를 데이터베이스에 저장 또는 업데이트
        SiteUser user = userRepository.findByEmail(email).get();
        if (user == null) {
            // 새로운 사용자 등록
            user = new SiteUser();
            user.setEmail(email);
            user.setNickname(nickname); // 혹은 다른 필드에 맞게 수정
            userRepository.save(user);
        } else {
            // 기존 사용자 업데이트 (필요한 경우)
            user.setNickname(nickname);
            userRepository.save(user);
        }
    }

    public void assignRoleToUser(String email, UserRole role) {
        Optional<SiteUser> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            SiteUser user = optionalUser.get();
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }
}
