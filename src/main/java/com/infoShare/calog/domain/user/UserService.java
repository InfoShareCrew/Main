package com.infoShare.calog.domain.user;

import com.infoShare.calog.domain.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                .createdDate(LocalDateTime.now())
                .build();
        return userRepository.save(siteUser);
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String email, String nickname, String password) {
        SiteUser user = findByEmail(email);

        if (user != null) {
            return user; // 기존 사용자 반환
        }

        // 비밀번호가 null이거나 빈 문자열인 경우 랜덤 비밀번호 생성
        String finalPassword = (password == null || password.isEmpty()) ? generateRandomPassword() : password;
        return join(email, finalPassword, nickname); // 최초 로그인 시 딱 한번 실행
    }

    // 랜덤 비밀번호 생성 메서드
    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10); // 10자리 랜덤 비밀번호
    }

    public SiteUser findByEmail(String email) {
        Optional<SiteUser> _user = userRepository.findByEmail(email);
        if (_user.isPresent()) {
            return userRepository.findByEmail(email).get();
        }
        return null;
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

    public Long findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(SiteUser::getId)
                .orElse(null); // User가 없으면 null 반환
    }

    public void modifypassword(Long userId, String password) {
        // 1. 기존 사용자 조회
        SiteUser siteUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found")); // 사용자 없을 경우 예외 처리

        // 2. 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(password);

        // 3. 빌더를 사용하여 기존 정보와 수정된 비밀번호로 새 객체 생성
        SiteUser updatedUser = SiteUser.builder()
                .id(siteUser.getId()) // 기존 ID 유지
                .intro(siteUser.getIntro()) // 기존 intro 유지
                .address(siteUser.getAddress()) // 기존 address 유지
                .profileImg(siteUser.getProfileImg()) // 기존 profileImg 유지
                .nickname(siteUser.getNickname()) // 기존 사용자명 유지
                .email(siteUser.getEmail()) // 기존 이메일 유지
                .cafe(siteUser.getCafe()) // 기존 카페 유지
                .password(encodedPassword) // 인코딩된 비밀번호 설정
                .createdDate(siteUser.getCreatedDate()) // 기존 생성 날짜 유지
                .modifiedDate(LocalDateTime.now()) // 수정 날짜 업데이트
                .build();

        // 4. 사용자 정보 저장
        userRepository.save(updatedUser); // 새로운 사용자 정보 저장
    }

    public Optional<SiteUser> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
