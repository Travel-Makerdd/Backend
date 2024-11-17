package com.hsu.travelmaker.domain.user.web.controller;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.service.UserServiceImpl;
import com.hsu.travelmaker.domain.user.web.dto.SignInDto;
import com.hsu.travelmaker.domain.user.web.dto.SignUpDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<CustomApiResponse<?>> signUp(@Valid @RequestBody SignUpDto dto) {
        return userService.signUp(dto);
    }

    // 로그인
    @PostMapping("/signIn")
    public ResponseEntity<CustomApiResponse<?>> signIn(@RequestBody SignInDto dto) {
        return userService.signIn(dto);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<CustomApiResponse<?>> withdraw() {
        return userService.withdraw();
    }

}
