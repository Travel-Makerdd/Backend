package com.hsu.travelmaker.domain.profile.web.dto;

import com.hsu.travelmaker.domain.profile.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateDto {
    @NotBlank(message = "역할을 입력해주세요.")
    private Role profileRole;

    @NotBlank(message = "자기소개를 입력해주세요.")
    private String profileBio;

    @NotBlank(message = "여행스타일을 입력해주세요.")
    private String profileStyle;

    @NotBlank(message = "선호여행지를 입력해주세요.")
    private String profileFavorite;

}
