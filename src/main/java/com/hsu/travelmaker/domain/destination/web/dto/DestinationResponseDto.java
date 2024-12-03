package com.hsu.travelmaker.domain.destination.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationResponseDto {
    private Long destinationId; // 여행지 PK
    private String destinationName; // 여행지 이름
    private String destinationDescription; // 여행지 설명
    private String destinationLocation; // 여행지 위치
    private List<String> destinationImageUrl; // 이미지 URL 리스트
}
