package com.hsu.travelmaker.domain.user.service;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.domain.user.web.dto.SignUpDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> signUp(SignUpDto dto) {

        if (userRepository.findByUserEmail(dto.getUserEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.createFailWithout(409, "이미 등록된 이메일입니다."));
        }

        User user = User.builder()
                .userEmail(dto.getUserEmail())
                .userPassword(passwordEncoder.encode(dto.getUserPassword()))
                .build();
        userRepository.save(user);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "사용자 등록에 성공했습니다."));
    }

}
