package com.hsu.travelmaker.domain.trip.service;

import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.domain.trip.web.dto.TripCreateDto;
import com.hsu.travelmaker.domain.trip.web.dto.TripResponseDto;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final AuthenticationUserUtils authenticationUserUtils;
    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;
    private final TripImageRepository tripImageRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createTrip(TripCreateDto dto) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 날짜 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tripStart = null;
        Date tripEnd = null;
        try {
            tripStart = dto.getStartDate();
            tripEnd = dto.getEndDate();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(400, "날짜 형식이 잘못되었습니다."));
        }

        // 여행 상품 생성
        Trip trip = Trip.builder()
                .user(user)
                .tripTitle(dto.getTripTitle())
                .tripDescription(dto.getTripDescription())
                .tripPrice(dto.getTripPrice())
                .tripStart(tripStart)
                .tripEnd(tripEnd)
                .build();

        // Trip 저장
        tripRepository.save(trip);
        // 일정 및 활동 추가
        for (ScheduleResponseDto scheduleDto : dto.getSchedules()) {
            Schedule schedule = new Schedule();
            schedule.setScheduleDay(scheduleDto.getScheduleDay());
            schedule.setTrip(trip);  // 일정에 해당 여행 상품 설정

            // 활동 추가
            for (ActivityResponseDto activityDto : scheduleDto.getActivities()) {
                Activity activity = new Activity();
                activity.setActivityTime(activityDto.getActivityTime());
                activity.setActivityTitle(activityDto.getActivityTitle());
                activity.setActivityContent(activityDto.getActivityContent());
                activity.setActivityExpense(activityDto.getActivityExpense());
                activity.setSchedule(schedule);  // 활동에 해당 일정 설정
                schedule.getActivities().add(activity);  // 일정에 활동 추가
            }

            // Schedule 저장
            scheduleRepository.save(schedule);
        }
        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "여행 상품이 성공적으로 생성되었습니다."));
    }
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getAllTrips() {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 모든 여행 상품 조회
        List<Trip> trips = tripRepository.findAll();

        // 조회한 여행 상품 리스트를 DTO로 변환
        List<TripResponseDto> tripResponseDtos = trips.stream()
                .map(trip -> TripResponseDto.builder()
                        .tripId(trip.getTripId())
                        .tripTitle(trip.getTripTitle())
                        .tripDescription(trip.getTripDescription())
                        .userId(trip.getUser().getUserId())
                        .tripPrice(trip.getTripPrice())
                        .startDate(trip.getTripStart())
                        .endDate(trip.getTripEnd())
                        .tripImageUrls(tripImageRepository.findByTripId(trip).stream()
                                .map(TripImage::getTripImageUrl)
                                .collect(Collectors.toList())) // 여행 상품 이미지 URL들
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, tripResponseDtos, "여행 상품 목록 조회 성공"));
    }
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getTripById(Long tripId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }
        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 특정 여행 상품 조회
        Trip trip = tripRepository.findByTripId(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행 상품을 찾을 수 없습니다."));

        // 해당 여행 상품에 대한 일정 조회 (일차별로 그룹화)
        List<Schedule> schedules = scheduleRepository.findByTrip(trip);

        // 일차별로 일정 그룹화
        Map<Integer, List<ScheduleResponseDto>> scheduleGroupedByDay = schedules.stream()
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

        // 반환 데이터
        TripResponseDto data = TripResponseDto.builder()
                .tripId(trip.getTripId())
                .tripTitle(trip.getTripTitle())
                .tripDescription(trip.getTripDescription())
                .userId(trip.getUser().getUserId())  // 사용자 ID
                .tripImageUrls(tripImageUrls)
                .tripPrice(trip.getTripPrice())
                .startDate(trip.getTripStart())
                .endDate(trip.getTripEnd())
                .schedules(scheduleGroupedByDay)  // 일차별 일정 추가
                .build();

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, data, "여행 상품 및 일정 조회 성공"));
    }

}
