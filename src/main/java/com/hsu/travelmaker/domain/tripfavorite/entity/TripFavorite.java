package com.hsu.travelmaker.domain.tripfavorite.entity;

import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TRIPFAVORITE")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TripFavorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_favorite_id")
    private Long tripFavoriteId; // 여행 즐겨찾기 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId; // 사용자 FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip tripId; // 여행상품 FK
}