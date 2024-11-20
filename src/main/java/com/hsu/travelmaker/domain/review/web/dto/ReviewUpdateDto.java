package com.hsu.travelmaker.domain.review.web.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ReviewUpdateDto {
    @NotBlank(message="여행상품 ID를 입력해주세요.")
    private Long tripId;

    @NotBlank(message="리뷰 점수를 입력해주세요.")
    private Integer reviewRating;

    @NotBlank(message="리뷰 내용을 입력해주세요.")
    private String reviewContent;
}
