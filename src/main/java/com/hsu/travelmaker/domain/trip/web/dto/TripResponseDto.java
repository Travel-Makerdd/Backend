package com.hsu.travelmaker.domain.trip.web.dto;

import com.hsu.travelmaker.domain.schedule.web.dto.ScheduleResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private List<String> tripImageUrls;
    private BigDecimal tripPrice;
    private Date startDate;
    private Date endDate;
    private Map<Integer, List<ScheduleResponseDto>> schedules;
}
