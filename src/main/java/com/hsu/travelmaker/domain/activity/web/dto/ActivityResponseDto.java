package com.hsu.travelmaker.domain.activity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ActivityResponseDto {
    private String activityTime; // 활동 시간
    private String activityTitle; // 활동 제목
    private BigDecimal activityExpense; // 활동 비용
    private String activityContent; // 활동 내용
}
