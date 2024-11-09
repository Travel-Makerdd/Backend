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

        // 이메일 중복 확인
        if (userRepository.findByUserEmail(dto.getUserEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.createFailWithout(409, "이미 등록된 이메일입니다."));
        }

        // 비밀번호 일치 확인
        if (!dto.getUserPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(400, "비밀번호가 일치하지 않습니다."));
        }

        User user = User.builder()
                .userEmail(dto.getUserEmail())
                .userPassword(passwordEncoder.encode(dto.getUserPassword()))
                .userNickname(dto.getUserNickname())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "사용자 등록에 성공했습니다."));
    }

}
