package com.hsu.travelmaker.domain.schedule.web.dto;

import com.hsu.travelmaker.domain.activity.web.dto.ActivityResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    private Long scheduleId;
    private String tripTitle; // 여행 상품 제목
    private Integer scheduleDay; // 일정 일차
    private List<ActivityResponseDto> activities; // 활동 리스트
}
