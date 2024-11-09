package com.hsu.travelmaker.domain.profile.web.controller;

import com.hsu.travelmaker.domain.profile.service.ProfileServiceImpl;
import com.hsu.travelmaker.domain.profile.web.dto.ProfileUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;
    

    @PostMapping("/update")
    public ResponseEntity<CustomApiResponse<?>> updateProfile(@RequestBody ProfileUpdateDto dto) {
        return profileService.updateProfile(dto);
    }

    @GetMapping("/check")
    public ResponseEntity<CustomApiResponse<?>> getProfile(@RequestBody ProfileCheckDto dto) {
        return profileService.getProfile(dto);
    }

}
