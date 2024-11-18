package com.hsu.travelmaker.domain.reservation.service;

import com.hsu.travelmaker.domain.reservation.entity.Reservation;
import com.hsu.travelmaker.domain.reservation.repository.ReservationRepository;
import com.hsu.travelmaker.domain.reservation.web.dto.ReservationResponseDto;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final AuthenticationUserUtils authenticationUserUtils;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final TripRepository tripRepository;
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createReservation(Long tripId) {
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

        // 여행상품 조회
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행상품을 찾을 수 없습니다."));

        // 예약 생성 및 저장
        Reservation reservation = Reservation.builder()
                .userId(user)
                .tripId(trip)
                .build();
        reservationRepository.save(reservation);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "예약이 성공적으로 생성되었습니다."));

    }
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> deleteReservation(Long reservationId){
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

        // 특정 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행 상품을 찾을 수 없습니다."));
        // 즐겨찾기 삭제
        reservationRepository.delete(reservation);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "예약이 취소되었습니다."));
    }

}
