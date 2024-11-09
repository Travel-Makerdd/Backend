package com.hsu.travelmaker.domain.profile.service;

import com.hsu.travelmaker.domain.profile.web.dto.ProfileCheckDto;
import com.hsu.travelmaker.domain.profile.web.dto.ProfileUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    ResponseEntity<CustomApiResponse<?>> updateProfile(ProfileUpdateDto dto);
    ResponseEntity<CustomApiResponse<?>> getProfile();
}
