package com.infoShare.calog.domain.user.service;

import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.user.SiteUser;
import com.infoShare.calog.domain.user.UserRepository;
import com.infoShare.calog.domain.user.UserRole;
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

    @Transactional
    public SiteUser processKakaoUser(String email, String nickname) {
        // 사용자 조회
        SiteUser user = userRepository.findByEmail(email).get();

        // 사용자가 존재하지 않으면 새 사용자 생성
        if (user == null) {
            // 새로운 사용자 등록
            SiteUser siteUser = SiteUser
                    .builder()
                    .email(email)
                    .password(passwordEncoder.encode("")) // 비밀번호를 빈 문자열로 설정
                    .nickname(nickname)
                    .build();
            this.userRepository.save(siteUser);
        } else {
            // 기존 사용자 업데이트 (필요한 경우)
            SiteUser siteUser = SiteUser
                    .builder()
                    .nickname(nickname)
                    .build();
            this.userRepository.save(siteUser);
        }

        return user;
    }

    @Transactional
    public void processGoogleUser(String email, String nickname) {
        // 사용자 정보를 데이터베이스에 저장 또는 업데이트
        SiteUser user = userRepository.findByEmail(email).get();
        if (user == null) {
            // 새로운 사용자 등록
            SiteUser siteUser = SiteUser
                    .builder()
                    .email(email)
                    .password(passwordEncoder.encode("")) // 비밀번호를 빈 문자열로 설정
                    .nickname(nickname)
                    .build();
            this.userRepository.save(siteUser);
        } else {
            // 기존 사용자 업데이트 (필요한 경우)
            SiteUser siteUser = SiteUser
                    .builder()
                    .nickname(nickname)
                    .build();
            this.userRepository.save(siteUser);
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

    public SiteUser join(String email, String password, String nickname) {
        SiteUser siteUser = SiteUser
                .builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 입력받은 비밀번호로 설정
                .nickname(nickname)
                .build();
        return this.userRepository.save(siteUser);
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String email, String nickname) {
        Optional<SiteUser> opMember = userRepository.findByEmail(email);
        if (opMember.isPresent()) {
            return opMember.get();
        }

        // 사용자 가입 시 기본 비밀번호를 사용
        return join(email, "defaultPassword", nickname); // "defaultPassword" 대신 다른 임시 비밀번호 사용 가능
    }

    public SiteUser getUser(String email) {
        Optional<SiteUser> siteUser = this.userRepository.findByEmail(email);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
