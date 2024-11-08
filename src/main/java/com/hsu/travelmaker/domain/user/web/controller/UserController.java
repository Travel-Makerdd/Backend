package com.hsu.travelmaker.domain.user.web.controller;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.service.UserServiceImpl;
import com.hsu.travelmaker.domain.user.web.dto.SignUpDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/signUp")
    public ResponseEntity<CustomApiResponse<?>> signUp(@Valid @RequestBody SignUpDto dto) {
        return userService.signUp(dto);
    }

}
