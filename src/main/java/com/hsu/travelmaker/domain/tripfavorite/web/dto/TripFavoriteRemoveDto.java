package com.hsu.travelmaker.domain.tripfavorite.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TripFavoriteRemoveDto {
    @NotBlank(message = "여행상품 즐겨찾기 ID를 입력해주세요.")
    private Long tripFavoriteId;
}
