package com.hsu.travelmaker.domain.user.web.dto;

import jakarta.validation.constraints.NotBlank;

public class SignInDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String userEmail;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String userPassword;

}
