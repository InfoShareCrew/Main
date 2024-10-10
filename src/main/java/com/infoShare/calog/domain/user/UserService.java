package com.infoShare.calog.domain.user;

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

    public SiteUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null); // Optional을 null로 변환
    }

    public boolean validateUser(String email, String password) {
        SiteUser user = findByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public SiteUser processKakaoUser(String email, String nickname) {
        SiteUser user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new SiteUser();
            user.setEmail(email);
            user.setNickname(nickname);
            userRepository.save(user);
        } else {
            user.setNickname(nickname);
            userRepository.save(user);
        }

        return user;
    }

    @Transactional
    public void processGoogleUser(String email, String nickname) {
        SiteUser user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new SiteUser();
            user.setEmail(email);
            user.setNickname(nickname);
            userRepository.save(user);
        } else {
            user.setNickname(nickname);
            userRepository.save(user);
        }
    }
}
