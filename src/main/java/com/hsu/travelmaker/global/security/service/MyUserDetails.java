package com.hsu.travelmaker.global.security.service;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));

        // 권한 정보 추가
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(), authorities);
    }
}
