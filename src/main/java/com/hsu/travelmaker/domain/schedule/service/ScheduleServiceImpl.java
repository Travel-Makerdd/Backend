package com.hsu.travelmaker.domain.schedule.service;

import com.hsu.travelmaker.domain.schedule.entity.Schedule;
import com.hsu.travelmaker.domain.schedule.repository.ScheduleRepository;
import com.hsu.travelmaker.domain.schedule.web.dto.ScheduleResponseDto;
import com.hsu.travelmaker.domain.activity.repository.ActivityRepository;
import com.hsu.travelmaker.domain.activity.web.dto.ActivityResponseDto;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService { // ScheduleService 인터페이스 구현

    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;
    private final AuthenticationUserUtils authenticationUserUtils;
    private final TripRepository tripRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getScheduleByTripId(Long tripId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 여행 상품 조회 (tripId로 정확히 여행 상품을 찾기)
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행 상품을 찾을 수 없습니다."));

        // 해당 여행 상품에 대한 일정 조회
        List<Schedule> schedules = scheduleRepository.findByTrip(trip);

        // 만약 일정을 찾지 못한 경우 처리
        if (schedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomApiResponse.createFailWithout(404, "해당 여행 상품에 대한 일정이 없습니다."));
        }

        // 일차별로 활동을 그룹화
        Map<Integer, List<ScheduleResponseDto>> scheduleByDay = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getScheduleDay,
                        Collectors.mapping(schedule -> {
                            List<ActivityResponseDto> activityDtos = activityRepository.findBySchedule(schedule).stream()
                                    .map(activity -> new ActivityResponseDto(
                                            activity.getActivityTime(),
                                            activity.getActivityTitle(),
                                            activity.getActivityExpense(),
                                            activity.getActivityContent()))
                                    .collect(Collectors.toList());
                            return new ScheduleResponseDto(schedule.getScheduleId(), trip.getTripTitle(), schedule.getScheduleDay(), activityDtos);
                        }, Collectors.toList())));

        // 일차별로 묶은 일정을 반환
        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, scheduleByDay, "여행 일정 일차별 조회 성공"));
    }


}
