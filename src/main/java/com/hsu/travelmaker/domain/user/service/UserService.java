package com.hsu.travelmaker.domain.user.service;

import com.hsu.travelmaker.domain.user.web.dto.SignInDto;
import com.hsu.travelmaker.domain.user.web.dto.SignUpDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<CustomApiResponse<?>> signUp(SignUpDto dto);
    ResponseEntity<CustomApiResponse<?>> signIn(SignInDto dto);
    ResponseEntity<CustomApiResponse<?>> withdraw();
}
