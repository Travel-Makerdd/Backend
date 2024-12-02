package com.hsu.travelmaker.domain.user.service;

import com.hsu.travelmaker.domain.post.repository.PostRepository;
import com.hsu.travelmaker.domain.profile.entity.Profile;
import com.hsu.travelmaker.domain.profile.entity.Role;
import com.hsu.travelmaker.domain.profile.repository.ProfileRepository;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.domain.user.web.dto.SignInDto;
import com.hsu.travelmaker.domain.user.web.dto.SignInResponseDto;
import com.hsu.travelmaker.domain.user.web.dto.SignUpDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.JwtTokenProvider;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationUserUtils authenticationUserUtils; // 현재 로그인된 사용자 정보를 가져옴

    @PersistenceContext
    private EntityManager entityManager;


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

        // 빈 프로필 생성 및 저장
        Profile profile = Profile.builder()
                .user(user)
                .profileRole(Role.DEFAULT)
                .profileBio("")
                .profileStyle("")
                .profileFavorite("")
                .build();
        profileRepository.save(profile);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "사용자 등록에 성공했습니다."));
    }

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> signIn(SignInDto dto) {
        // 이메일로 사용자 조회
        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "비밀번호가 일치하지 않습니다."));
        }

        // 토큰 생성
        String token = jwtTokenProvider.createToken(user.getUserId().toString());

        // 응답 DTO 생성
        SignInResponseDto responseDto = new SignInResponseDto(token, user.getUserNickname());

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, responseDto, "로그인에 성공했습니다."));
    }

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> withdraw() {
        String currentUserId = authenticationUserUtils.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰입니다."));
        }

        Long userId = Long.parseLong(currentUserId);

        // 외래 키 체크 비활성화
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        // 사용자 삭제
        userRepository.deleteUserDirectly(userId);

        // 외래 키 체크 다시 활성화
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "회원 탈퇴가 완료되었습니다."));
    }
}
