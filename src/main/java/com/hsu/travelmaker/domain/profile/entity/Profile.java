package com.hsu.travelmaker.domain.profile.entity;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PROFILE")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId; // 프로필 PK

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 FK

    @Column(name = "profile_name", nullable = false)
    private String profileName; // 이름

    @Column(name = "profile_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role profileRole; // 역할

    @Column(name = "profile_bio")
    private String profileBio; // 자기소개

    @Column(name = "profile_style")
    private String profileStyle; // 여행 스타일

    @Column(name = "profile_favorite")
    private String profileFavorite; // 선호 여행지

}
