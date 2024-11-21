package com.hsu.travelmaker.domain.user.entity;

import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "USER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // 사용자 PK

    @Column(name = "user_email", nullable = false)
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "유효한 이메일 형식이 아닙니다.")
    private String userEmail; // 사용자 이메일

    @Column(name = "user_password", nullable = false)
    private String userPassword; // 사용자 비밀번호

    @Column(name = "user_nickname", nullable = false)
    private String userNickname; // 사용자 닉네임
}
