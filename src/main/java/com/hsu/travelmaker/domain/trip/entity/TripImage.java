package com.hsu.travelmaker.domain.trip.entity;

import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TRIPIMAGE")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

public class TripImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_image_id")
    private Long tripImageId; // 게시글 이미지 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip tripId; // 게시글 FK

    @Column(name = "trip_image_url", nullable = false)
    private String tripImageUrl; // 게시글 제목

}





