package com.hsu.travelmaker.domain.reservation.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDeleteDto {
    @NotBlank(message="예약 ID를 입력해주세요.")
    private Long reservationId;
}
