package com.hsu.travelmaker.domain.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResponseDto {
    private String token;
    private String nickname;
}
