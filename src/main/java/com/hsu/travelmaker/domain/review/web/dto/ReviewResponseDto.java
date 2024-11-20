package com.hsu.travelmaker.domain.review.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long reviewId; // 리뷰 pk
    private Long tripId; // 여행상품 fk
    private Long userId; // 사용자 fk
    private String tripTitle;
    private Integer reviewRating;
    private String reviewContent;
    private LocalDateTime updatedAt;
}
