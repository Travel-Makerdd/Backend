package com.hsu.travelmaker.domain.trip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hsu.travelmaker.domain.schedule.entity.Schedule;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "trip_start", nullable = false)
    private Date tripStart; // 여행 시작일

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "trip_end", nullable = false)
    private Date tripEnd; // 여행 종료일

}
