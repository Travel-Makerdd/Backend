package com.hsu.travelmaker.domain.profile.service;

import com.hsu.travelmaker.domain.profile.entity.Profile;
import com.hsu.travelmaker.domain.profile.repository.ProfileRepository;
import com.hsu.travelmaker.domain.profile.web.dto.ProfileUpdateDto;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final AuthenticationUserUtils authenticationUserUtils;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> updateProfile(ProfileUpdateDto dto) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자와 해당 사용자의 프로필 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        Profile profile = profileRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로필이 존재하지 않습니다."));

        // 프로필 정보 업데이트
        profile.setProfileName(dto.getProfileName());
        profile.setProfileRole(dto.getProfileRole());
        profile.setProfileBio(dto.getProfileBio());
        profile.setProfileStyle(dto.getProfileStyle());
        profile.setProfileFavorite(dto.getProfileFavorite());

        // 변경 사항 저장
        profileRepository.save(profile);

        // 성공 응답 반환
        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "프로필이 성공적으로 업데이트되었습니다."));
    }

}
