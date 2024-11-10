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

        // tripPrice는 BigDecimal로 DTO에서 받아왔으므로 직접 사용
        BigDecimal tripPrice = dto.getTripPrice();

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
                .tripPrice(tripPrice) // BigDecimal 직접 사용
                .tripStart(tripStart)
                .tripEnd(tripEnd)
                .build();

        // Trip 저장
        tripRepository.save(trip);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "여행 상품이 성공적으로 생성되었습니다."));
    }

}
