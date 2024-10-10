package com.infoShare.calog.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<SiteUser> optionalSiteUser = userRepository.findByEmail(email);
        if (optionalSiteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        SiteUser siteUser = optionalSiteUser.get();

        // 사용자의 권한을 가져옴
        Collection<GrantedAuthority> authorities = siteUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getValue()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(siteUser.getEmail(), siteUser.getPassword(), authorities);
    }
}
