package com.hsu.travelmaker.domain.profile.web.dto;

import com.hsu.travelmaker.domain.profile.entity.Role;
import com.hsu.travelmaker.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileCheckDto {
    private Long userId;
    private String profileName;
    private Role profileRole;
    private String profileBio;
    private String profileStyle;
    private String profileFavorite;
}