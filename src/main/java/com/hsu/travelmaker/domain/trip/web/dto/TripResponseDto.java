package com.hsu.travelmaker.domain.trip.web.dto;

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
public class TripResponseDto {
    private Long tripId;
    private String tripTitle;
    private String tripDescription;
    private Long userId; // 사용자 fk
    private BigDecimal tripPrice;
    private Date startDate;
    private Date endDate;
    private Map<Integer, List<ScheduleResponseDto>> schedules;
}
