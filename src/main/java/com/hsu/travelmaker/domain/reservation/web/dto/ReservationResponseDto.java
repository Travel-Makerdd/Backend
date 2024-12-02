package com.hsu.travelmaker.domain.reservation.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDto {
    private Long reservationId;
    private Long tripId; // 여행상품 fk
    private String tripTitle;
    private String tripDescription;
    private Long userId; // 사용자 fk
    private BigDecimal tripPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}
