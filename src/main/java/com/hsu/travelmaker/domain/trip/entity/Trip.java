package com.hsu.travelmaker.domain.trip.entity;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TRIP")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

public class Trip extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId; // 여행 상품 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 FK

    @Column(name = "trip_title", nullable = false)
    private String tripTitle; // 여행 상품 제목

    @Column(name = "trip_description", nullable = false)
    private String tripDescription; // 여행 상품 설명

    @Column(name = "trip_price", nullable = false)
    private BigDecimal tripPrice; // 여행 상품 가격

    @Column(name = "trip_start", nullable = false)
    private Date tripStart; // 여행 시작일

    @Column(name = "trip_end", nullable = false)
    private Date tripEnd; // 여행 종료일

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();  // 여행 상품에 포함된 일정 리스트

}
