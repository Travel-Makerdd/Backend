package com.hsu.travelmaker.domain.trip.web.dto;

import com.hsu.travelmaker.domain.schedule.web.dto.ScheduleResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private Long userId;
    private List<String> tripImageUrls; // 이미지 스트림 URL 리스트
    private BigDecimal tripPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<Integer, List<ScheduleResponseDto>> schedules;
}
