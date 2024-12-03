package com.hsu.travelmaker.domain.destination.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DESTINATION")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Long destinationId; // 여행지 PK

    @Column(name = "destination_name", nullable = false)
    private String destinationName; // 여행지 이름

    @Lob
    @Column(name = "destination_description", nullable = false)
    private String destinationDescription; // 여행지 설명
    
    @Column(name = "destination_location", nullable = false)
    private String destinationLocation; // 여행지 위치
}
