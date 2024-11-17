package com.hsu.travelmaker.domain.activity.service;

import com.hsu.travelmaker.domain.activity.entity.Activity;
import com.hsu.travelmaker.domain.activity.repository.ActivityRepository;
import com.hsu.travelmaker.domain.activity.web.dto.ActivityResponseDto;
import com.hsu.travelmaker.domain.schedule.entity.Schedule;
import com.hsu.travelmaker.domain.schedule.repository.ScheduleRepository;
import com.hsu.travelmaker.domain.schedule.web.dto.ScheduleResponseDto;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl {

    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;
    private final TripRepository tripRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getScheduleByTripId(Long tripId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }
        // 여행 상품에 해당하는 일정 조회
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        List<Schedule> schedules = scheduleRepository.findByTrip(trip);

        // 각 일정에 대해 활동을 조회하여 포함
        List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(schedule -> {
                    // 해당 일정에 속한 활동 조회
                    List<Activity> activities = activityRepository.findBySchedule(schedule);
                    List<ActivityResponseDto> activityDtos = activities.stream()
                            .map(activity -> new ActivityResponseDto(
                                    activity.getActivityTime(),
                                    activity.getActivityTitle(),
                                    activity.getActivityExpense(),
                                    activity.getActivityContent()))
                            .collect(Collectors.toList());

                    // tripTitle을 포함하여 ScheduleResponseDto 생성
                    return new ScheduleResponseDto(
                            schedule.getScheduleId(),
                            schedule.getTrip().getTripTitle(),
                            schedule.getScheduleDay(),
                            activityDtos
                    );
                })
                .collect(Collectors.toList());

        // 응답 반환
        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, scheduleResponseDtos, "여행 일정 일차별 조회 성공"));
    }

}
