package com.hsu.travelmaker.domain.schedule.web.controller;

import com.hsu.travelmaker.domain.schedule.service.ScheduleServiceImpl;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleServiceImpl scheduleService;

    // 여행 상품에 대한 일차별 일정 조회
    @GetMapping("/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> getScheduleByTripId(@PathVariable Long tripId) {
        return scheduleService.getScheduleByTripId(tripId);  // 일차별 일정과 활동 반환
    }
}
