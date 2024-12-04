package com.hsu.travelmaker.domain.trip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsu.travelmaker.domain.activity.entity.Activity;
import com.hsu.travelmaker.domain.activity.repository.ActivityRepository;
import com.hsu.travelmaker.domain.activity.web.dto.ActivityResponseDto;
import com.hsu.travelmaker.domain.schedule.entity.Schedule;
import com.hsu.travelmaker.domain.schedule.repository.ScheduleRepository;
import com.hsu.travelmaker.domain.schedule.web.dto.ScheduleResponseDto;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.entity.TripImage;
import com.hsu.travelmaker.domain.trip.repository.TripImageRepository;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.domain.trip.web.dto.TripResponseDto;
import com.hsu.travelmaker.domain.tripfavorite.repository.TripFavoriteRepository;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/images/";
    private final TripFavoriteRepository tripFavoriteRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createTrip(
            String tripTitle,
            String tripDescription,
            BigDecimal tripPrice,
            String startDate,
            String endDate,
            String schedulesJson,
            List<MultipartFile> images
    ) {
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
        try {
            // 1. schedules JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            List<ScheduleResponseDto> schedules = objectMapper.readValue(schedulesJson, new TypeReference<List<ScheduleResponseDto>>() {
            });

            // 2. Trip 엔티티 생성 및 저장
            Trip trip = Trip.builder()
                    .user(user)
                    .tripTitle(tripTitle)
                    .tripDescription(tripDescription)
                    .tripPrice(tripPrice)
                    .tripStart(LocalDate.parse(startDate))
                    .tripEnd(LocalDate.parse(endDate))
                    .build();
            tripRepository.save(trip);

            // 3. 이미지 파일 저장
            if (images != null && !images.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/images/";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs(); // 디렉토리 생성
                }

                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        // 1. 업로드 시간 기반 파일 이름 생성
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date()); // "20241201_103015123" 형식
                        String fileExtension = ""; // 확장자 초기화
                        String originalFilename = image.getOriginalFilename();
                        // 2. 파일 확장자 추출
                        if (originalFilename != null && originalFilename.contains(".")) {
                            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                        }

                        // 3. 최종 파일 이름 생성
                        String newFilename = timeStamp + fileExtension; // 예: "20241201_103015123.png"

                        // 4. 저장 경로 생성
                        String filePath = uploadDir + newFilename;
                        File dest = new File(filePath);
                        image.transferTo(dest);

                        String imageUrl = "http://localhost:8080/images/" + newFilename;

                        TripImage tripImage = TripImage.builder()
                                .tripId(trip)
                                .images(imageUrl)
                                .build();
                        tripImageRepository.save(tripImage);
                    }
                }
            }

            // 4. 일정 및 활동 저장
            for (ScheduleResponseDto scheduleDto : schedules) {
                Schedule schedule = new Schedule();
                schedule.setScheduleDay(scheduleDto.getScheduleDay());
                schedule.setTrip(trip);

                for (ActivityResponseDto activityDto : scheduleDto.getActivities()) {
                    Activity activity = new Activity();
                    activity.setActivityTime(activityDto.getActivityTime());
                    activity.setActivityTitle(activityDto.getActivityTitle());
                    activity.setActivityContent(activityDto.getActivityContent());
                    activity.setActivityExpense(activityDto.getActivityExpense());
                    activity.setSchedule(schedule);
                    schedule.getActivities().add(activity);
                }

                scheduleRepository.save(schedule);
            }

            // 5. 성공 응답 반환
            return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "여행 상품 및 이미지 업로드 성공"));

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(400, "schedules JSON 파싱 오류: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(500, "서버 오류: " + e.getMessage()));
        }
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
                .map(trip -> {
                    // 이미지 URL 구성
                    List<String> tripImageUrls = tripImageRepository.findByTripId(trip)
                            .stream()
                            .map(tripImage -> "/api/trip/check/" + trip.getTripId() + "/image/" + extractFilename(tripImage.getImages()))
                            .collect(Collectors.toList());

                    // TripResponseDto 생성
                    return TripResponseDto.builder()
                            .tripId(trip.getTripId())
                            .tripTitle(trip.getTripTitle())
                            .tripDescription(trip.getTripDescription())
                            .userId(trip.getUser().getUserId())
                            .tripPrice(trip.getTripPrice())
                            .startDate(trip.getTripStart())
                            .endDate(trip.getTripEnd())
                            .tripImageUrls(tripImageUrls) // 이미지 URL 포함
                            .build();
                })
                .collect(Collectors.toList());

        // 성공 응답 반환
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

        try {
            // 여행 상품 조회
            Trip trip = tripRepository.findByTripId(tripId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행 상품을 찾을 수 없습니다."));

            // 이미지 URL 구성
            List<String> tripImageUrls = tripImageRepository.findByTripId(trip)
                    .stream()
                    .map(tripImage -> "/api/trip/check/" + trip.getTripId() + "/image/" + extractFilename(tripImage.getImages()))
                    .collect(Collectors.toList());

            // 일정 및 활동 데이터 처리
            Map<Integer, List<ScheduleResponseDto>> scheduleGroupedByDay = scheduleRepository.findByTrip(trip)
                    .stream()
                    .collect(Collectors.groupingBy(Schedule::getScheduleDay,
                            Collectors.mapping(schedule -> {
                                List<ActivityResponseDto> activityDtos = activityRepository.findBySchedule(schedule)
                                        .stream()
                                        .map(activity -> new ActivityResponseDto(
                                                activity.getActivityTime(),
                                                activity.getActivityTitle(),
                                                activity.getActivityExpense(),
                                                activity.getActivityContent()))
                                        .collect(Collectors.toList());
                                return new ScheduleResponseDto(schedule.getScheduleId(), trip.getTripTitle(), schedule.getScheduleDay(), activityDtos);
                            }, Collectors.toList())));
            // 즐겨찾기 여부 확인
            boolean isFavorite = tripFavoriteRepository.findByUserIdAndTripId(user, trip).isPresent();

            // TripResponseDto 생성
            TripResponseDto data = TripResponseDto.builder()
                    .tripId(trip.getTripId())
                    .tripTitle(trip.getTripTitle())
                    .tripDescription(trip.getTripDescription())
                    .userId(trip.getUser().getUserId())
                    .tripImageUrls(tripImageUrls)
                    .tripPrice(trip.getTripPrice())
                    .startDate(trip.getTripStart())
                    .endDate(trip.getTripEnd())
                    .schedules(scheduleGroupedByDay)
                    .isFavorite(isFavorite)
                    .build();

            // 성공 응답 반환
            return ResponseEntity.ok(CustomApiResponse.createSuccess(200, data, "여행 상품 및 이미지 데이터 조회 성공"));

        } catch (Exception e) {
            // 에러 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithout(500, "여행 상품 조회 중 오류 발생: " + e.getMessage()));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> getTripImage(Long tripId, String imageName) {
        try {
            // 파일 경로 생성
            Path filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "images", imageName);

            // 파일이 존재하지 않으면 404 반환
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            // 파일을 읽어서 바이너리 데이터를 반환
            byte[] imageBytes = Files.readAllBytes(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(Files.probeContentType(filePath) != null
                    ? MediaType.parseMediaType(Files.probeContentType(filePath))
                    : MediaType.APPLICATION_OCTET_STREAM); // 파일 유형 설정
            headers.setContentLength(imageBytes.length); // 파일 크기 설정

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    private String extractFilename(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}

