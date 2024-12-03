package com.hsu.travelmaker.domain.destination.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DESTINATION_IMAGE")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DestinationImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_image_id")
    private Long destinationImage; // 여행지 이미지 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination; // 여행지 FK

    @Column(name = "destination_image_url", nullable = false)
    private String destinationImageUrl; // 이미지 URL

}
