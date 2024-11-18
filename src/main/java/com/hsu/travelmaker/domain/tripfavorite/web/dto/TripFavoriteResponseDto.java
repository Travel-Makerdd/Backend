package com.hsu.travelmaker.domain.tripfavorite.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripFavoriteResponseDto {
    private Long tripFavoriteId;  // 즐겨찾기 ID
    private Long tripId; //여행상품 FK
    private String tripTitle;     // 여행 상품 제목
    private String tripDescription;  // 여행 상품 설명
}
